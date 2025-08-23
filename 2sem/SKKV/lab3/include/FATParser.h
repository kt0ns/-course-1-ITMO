#ifndef FATPARSER_H
#define FATPARSER_H

#include "FATEntry.h"

#include <QDateTime>
#include <QFile>
#include <QString>
#include <QVector>

class FATParser
{
  public:
	FATParser();
	FATParser(const FATParser& other);
	FATParser& operator=(const FATParser& other);
	~FATParser();

	bool loadImage(const QString& path);
	QVector< FatEntry* > readDirectory(quint32 cluster);
	FatEntry* rootEntry() { return p_root; }

	int fatType() const { return p_fatType; }
	quint32 rootCluster() const { return p_rootDirCluster; }

  private:
#pragma pack(push, 1)
	struct BiosParameterBlock
	{
		quint8 jump[3];
		char oemName[8];
		quint16 bytesPerSector;
		quint8 sectorsPerCluster;
		quint16 reservedSectors;
		quint8 numFATs;
		quint16 rootEntryCount;
		quint16 totalSectors16;
		quint8 media;
		quint16 fatSize16;
		quint16 sectorsPerTrack;
		quint16 numHeads;
		quint32 hiddenSectors;
		quint32 totalSectors32;
		quint32 fatSize32;
		quint16 extFlags;
		quint16 fsVersion;
		quint32 rootCluster;
		quint16 fsInfo;
		quint16 backupBootSector;
		quint8 reserved[12];
		quint8 driveNumber;
		quint8 reserved1;
		quint8 bootSignature;
		quint32 volumeID;
		char volumeLabel[11];
		char fsType[8];
	};
#pragma pack(pop)

	enum FATType
	{
		FAT_UNKNOWN,
		FAT12,
		FAT16,
		FAT32
	} p_fatType;

	QFile p_imageFile;
	quint32 p_rootDirCluster = 0;
	FatEntry* p_root = nullptr;

	QByteArray p_fatTable;
	bool p_isFAT16;
	QByteArray p_sector;
	BiosParameterBlock p_bpb;

	bool parseBootSector();
	bool parseFAT();
	FatEntry* findByCluster(FatEntry* node, quint16 cluster);
	quint32 fatEntry(quint32 cluster) const;
	quint64 clusterOffset(quint32 cluster) const;
	quint32 countClusters(quint32 start) const;
	FatEntry* parseDirectory(quint16 cluster);
	QString decodeLFN(const QVector< QByteArray >& entries);
	QDateTime parseTime(uint16_t timeRaw, uint16_t dateRaw);

	void deleteTree(FatEntry* node);
	void copy(const FATParser& other);
	FatEntry* copyTree(const FatEntry* node);
};

#endif	  // FATPARSER_H
