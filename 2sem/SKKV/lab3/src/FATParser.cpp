#include "FATParser.h"

#include <QDataStream>
#include <QDateTime>
#include <QDebug>
#include <QIODevice>
#include <QtEndian>
#include <cstring>

FATParser::FATParser() : p_fatType(FAT_UNKNOWN), p_root(nullptr), p_isFAT16(false) {}

FATParser::FATParser(const FATParser& other) : p_fatType(FAT_UNKNOWN), p_root(nullptr)
{
	copy(other);
}

FATParser& FATParser::operator=(const FATParser& other)
{
	if (this != &other)
	{
		deleteTree(p_root);
		copy(other);
	}
	return *this;
}

FATParser::~FATParser()
{
	deleteTree(p_root);
}

bool FATParser::loadImage(const QString& path)
{
	if (p_imageFile.isOpen())
	{
		p_imageFile.close();
	}

	p_imageFile.QFile::setFileName(path);
	if (!p_imageFile.open(QIODevice::ReadOnly))
	{
		qWarning() << "Failed to open image:" << path;
		return false;
	}

	if (!parseBootSector())
		return false;

	if (!parseFAT())
		return false;

	p_root = new FatEntry;
	p_root->name = (p_fatType == FAT32) ? "/" : "";
	p_root->isDirectory = true;
	p_root->startCluster = p_rootDirCluster;
	p_root->dataOffset = 0;
	p_root->clusterCount = 0;

	QVector< FatEntry* > children = readDirectory(p_rootDirCluster);
	for (FatEntry* e : children)
	{
		e->parent = p_root;
		p_root->children.append(e);
	}

	if (!p_root)
		return false;

	return true;
}

bool FATParser::parseBootSector()
{
	if (!p_imageFile.isOpen() || !p_imageFile.isReadable())
	{
		return false;
	}

	if (!p_imageFile.seek(0))
	{
		return false;
	}

	p_sector = p_imageFile.read(512);
	if (p_sector.size() != 512)
	{
		return false;
	}

	const quint8 sig0 = static_cast< quint8 >(p_sector.at(510));
	const quint8 sig1 = static_cast< quint8 >(p_sector.at(511));
	if (sig0 != 0x55 || sig1 != 0xAA)
	{
		return false;
	}

	std::memcpy(&p_bpb, p_sector.constData(), sizeof(BiosParameterBlock));

	const quint32 bytesPerSector = p_bpb.bytesPerSector;
	const quint32 sectorsPerCluster = p_bpb.sectorsPerCluster;
	const quint32 reservedSectors = p_bpb.reservedSectors;
	const quint32 numFATs = p_bpb.numFATs;
	const quint32 sectorsPerFAT = p_bpb.fatSize16 ? p_bpb.fatSize16 : p_bpb.fatSize32;
	const quint32 totalSectors = p_bpb.totalSectors16 ? p_bpb.totalSectors16 : p_bpb.totalSectors32;
	const quint32 rootDirEntries = p_bpb.rootEntryCount;
	const quint32 rootDirSectors = (rootDirEntries * 32 + bytesPerSector - 1) / bytesPerSector;

	const quint32 firstDataSector = reservedSectors + numFATs * sectorsPerFAT + rootDirSectors;

	const quint32 dataSectors = totalSectors - firstDataSector;

	const quint32 totalClusters = dataSectors / sectorsPerCluster;

	qDebug() << "count of clusters" << totalClusters;

	if (totalClusters < 4085)
		p_fatType = FAT12;
	else if (totalClusters < 65525)
		p_fatType = FAT16;
	else
		p_fatType = FAT32;

	if (p_fatType == FAT12)
	{
		qWarning() << "FAT12 not supported.";
		return false;
	}

	p_isFAT16 = (p_fatType == FAT16);

	if (p_isFAT16)
	{
		p_rootDirCluster = 0;
	}
	else
	{
		p_rootDirCluster = p_bpb.rootCluster;
	}

	return true;
}

bool FATParser::parseFAT()
{
	quint32 fatSize = p_bpb.fatSize16 ? p_bpb.fatSize16 : p_bpb.fatSize32;
	quint64 fatBytes = quint64(fatSize) * p_bpb.bytesPerSector;
	quint64 fatOffset = quint64(p_bpb.reservedSectors) * p_bpb.bytesPerSector;

	p_imageFile.seek(fatOffset);
	p_fatTable = p_imageFile.read(fatBytes);

	if (p_fatTable.size() != fatBytes)
	{
		return false;
	}
	return true;
}

quint32 FATParser::fatEntry(quint32 cluster) const
{
	const int entrySize = (p_fatType == FAT16) ? 2 : 4;
	const quint32 pos = cluster * entrySize;

	if (pos + entrySize > static_cast< quint32 >(p_fatTable.size()))
		return 0;

	quint32 value = 0;
	for (int i = 0; i < entrySize; ++i)
		value |= quint32(quint8(p_fatTable[pos + i])) << (8 * i);

	return (p_fatType == FAT16) ? value : (value & 0x0FFFFFFF);
}

quint64 FATParser::clusterOffset(quint32 cluster) const
{
	quint32 fatSectors = p_bpb.numFATs * (p_fatType == FAT16 ? p_bpb.fatSize16 : p_bpb.fatSize32);
	quint32 rootDirSectors = (p_fatType == FAT16) ? (p_bpb.rootEntryCount * 32 + p_bpb.bytesPerSector - 1) / p_bpb.bytesPerSector : 0;

	quint32 firstDataSector = p_bpb.reservedSectors + fatSectors + rootDirSectors;
	quint32 sector = firstDataSector + (cluster - 2) * p_bpb.sectorsPerCluster;

	return quint64(sector) * p_bpb.bytesPerSector;
}

quint32 FATParser::countClusters(quint32 start) const
{
	quint32 cnt = 0;
	quint32 cl = start;
	quint32 endMark = (p_fatType == FAT16) ? 0xFFF8 : 0x0FFFFFF8;

	while (cl >= 2 && cl < endMark)
	{
		++cnt;
		cl = fatEntry(cl);
	}
	return cnt;
}

QVector< FatEntry* > FATParser::readDirectory(quint32 start)
{
	bool isRoot16 = (p_fatType == FAT16 && start == 0);
	quint64 dirOffset;
	quint32 dirSize;
	if (isRoot16)
	{
		quint32 rootDirSectors = (p_bpb.rootEntryCount * 32 + p_bpb.bytesPerSector - 1) / p_bpb.bytesPerSector;
		quint32 firstRootSector = p_bpb.reservedSectors + p_bpb.numFATs * p_bpb.fatSize16;
		dirOffset = quint64(firstRootSector) * p_bpb.bytesPerSector;
		dirSize = rootDirSectors * p_bpb.bytesPerSector;
	}
	else
	{
		quint32 cluster = (isRoot16 ? 0 : start);
		dirOffset = clusterOffset(cluster);
		dirSize = countClusters(start) * p_bpb.sectorsPerCluster * p_bpb.bytesPerSector;
	}

	p_imageFile.seek(dirOffset);
	QByteArray data = p_imageFile.read(dirSize);

	FatEntry* dir = new FatEntry;
	dir->name = (p_fatType == FAT32 && start == p_rootDirCluster) ? "/" : QString();
	dir->isDirectory = true;
	dir->startCluster = quint16(start);
	dir->dataOffset = dirOffset;
	dir->clusterCount = isRoot16 ? dirSize / (p_bpb.bytesPerSector * p_bpb.sectorsPerCluster) : countClusters(start);

	const int entrySize = 32;
	QVector< QByteArray > lfnEntries;

	for (int offset = 0; offset + entrySize <= data.size(); offset += entrySize)
	{
		const char* ptr = data.constData() + offset;
		quint8 first = uchar(ptr[0]);
		if (first == 0x00)
			break;
		quint8 attr = uchar(ptr[11]);

		if (attr == 0x0F)
		{
			lfnEntries.append(QByteArray(ptr, entrySize));
			continue;
		}
		if (attr & 0x08)
		{
			lfnEntries.clear();
			continue;
		}

		QString name;
		if (!lfnEntries.isEmpty())
		{
			name = decodeLFN(lfnEntries);
			lfnEntries.clear();
		}
		else
		{
			name = QString::fromLatin1(ptr, 8).trimmed();
			QString ext = QString::fromLatin1(ptr + 8, 3).trimmed();
			if (!ext.isEmpty())
				name += "." + ext;
		}

		quint32 high = (p_fatType == FAT32) ? qFromLittleEndian< quint16 >(reinterpret_cast< const uchar* >(ptr + 20)) : 0;
		quint32 low = qFromLittleEndian< quint16 >(reinterpret_cast< const uchar* >(ptr + 26));
		quint32 startCl = (high << 16) | low;

		quint32 size = qFromLittleEndian< quint32 >(reinterpret_cast< const uchar* >(ptr + 28));

		quint16 timeRaw = qFromLittleEndian< quint16 >(reinterpret_cast< const uchar* >(ptr + 22));
		quint16 dateRaw = qFromLittleEndian< quint16 >(reinterpret_cast< const uchar* >(ptr + 24));

		FatEntry* e = new FatEntry;
		e->name = name;
		e->isDirectory = (attr & 0x10) != 0;
		e->size = size;
		e->time = parseTime(timeRaw, dateRaw);
		e->startCluster = quint16(startCl);
		e->dataOffset = (e->startCluster >= 2) ? clusterOffset(e->startCluster) : dirOffset;
		e->clusterCount = (e->isDirectory || size > 0) ? countClusters(startCl) : 0;
		e->parent = dir;
		dir->children.append(e);
	}

	QVector< FatEntry* > result = dir->children;
	delete dir;
	return result;
}

QString FATParser::decodeLFN(const QVector< QByteArray >& entries)
{
	QString name;

	for (int i = entries.size() - 1; i >= 0; --i)
	{
		const QByteArray& e = entries.at(i);
		static const int offsets[] = { 1, 14, 28 };
		static const int lengths[] = { 10, 12, 4 };

		for (int k = 0; k < 3; ++k)
		{
			int offset = offsets[k];
			int count = lengths[k];
			for (int j = 0; j < count; j += 2)
			{
				ushort wc = qFromLittleEndian< ushort >(reinterpret_cast< const uchar* >(e.constData() + offset + j));
				if (wc == 0x0000 || wc == 0xFFFF)
					return name;
				name.append(QChar(wc));
			}
		}
	}

	return name;
}

QDateTime FATParser::parseTime(uint16_t timeRaw, uint16_t dateRaw)
{
	int sec = (timeRaw & 0x1F) * 2;
	int min = (timeRaw >> 5) & 0x3F;
	int hour = (timeRaw >> 11) & 0x1F;

	int day = dateRaw & 0x1F;
	int month = (dateRaw >> 5) & 0x0F;
	int year = ((dateRaw >> 9) & 0x7F) + 1980;

	if (QDate::isValid(year, month, day) && QTime::isValid(hour, min, sec))
	{
		return QDateTime(QDate(year, month, day), QTime(hour, min, sec));
	}

	return QDateTime();
}

void FATParser::deleteTree(FatEntry* node)
{
	if (!node)
		return;
	for (FatEntry* child : node->children)
	{
		deleteTree(child);
	}
	delete node;
}

void FATParser::copy(const FATParser& other)
{
	p_fatType = other.p_fatType;
	p_rootDirCluster = other.p_rootDirCluster;
	p_fatTable = other.p_fatTable;
	p_isFAT16 = other.p_isFAT16;
	p_sector = other.p_sector;
	p_bpb = other.p_bpb;
	p_imageFile.setFileName(other.p_imageFile.fileName());

	p_root = other.p_root ? copyTree(other.p_root) : nullptr;
}

FatEntry* FATParser::copyTree(const FatEntry* node)
{
	if (!node)
		return nullptr;

	FatEntry* clone = new FatEntry;
	clone->name = node->name;
	clone->size = node->size;
	clone->time = node->time;
	clone->isDirectory = node->isDirectory;
	clone->startCluster = node->startCluster;
	clone->dataOffset = node->dataOffset;
	clone->clusterCount = node->clusterCount;

	for (const FatEntry* child : node->children)
	{
		FatEntry* clonedChild = copyTree(child);
		clonedChild->parent = clone;
		clone->children.append(clonedChild);
	}

	return clone;
}
