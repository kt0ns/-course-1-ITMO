#include "CustomFATFileSystemModel.h"

#include <QDebug>

CustomFATFileSystemModel::CustomFATFileSystemModel(QObject *parent) : QAbstractItemModel(parent) {}

bool CustomFATFileSystemModel::setRootPath(const QString &imagePath)
{
	if (!parser.loadImage(imagePath))
	{
		qWarning() << "Cannot load FAT image:" << imagePath;
		return false;
	}

	QAbstractItemModel::beginResetModel();

	clearTree(rootEntry);
	rootEntry = nullptr;

	FatEntry *src = parser.rootEntry();
	if (!src)
	{
		QAbstractItemModel::endResetModel();
		return false;
	}

	rootEntry = new FatEntry;
	rootEntry->parent = nullptr;
	rootEntry->name = QStringLiteral("FAT Image");

	FatEntry *realRoot = new FatEntry(*src);
	realRoot->parent = rootEntry;
	realRoot->children.clear();
	rootEntry->children.append(realRoot);

	buildTree(realRoot);

	QAbstractItemModel::endResetModel();

	return true;
}

bool CustomFATFileSystemModel::isDir(const QModelIndex &index) const
{
	if (!index.QModelIndex::isValid())
		return false;
	return static_cast< FatEntry * >(index.QModelIndex::internalPointer())->isDirectory;
}

QModelIndex CustomFATFileSystemModel::index(int row, int column, const QModelIndex &parent) const
{
	if (!QAbstractItemModel::hasIndex(row, column, parent))
		return QModelIndex();
	FatEntry *par = parent.isValid() ? static_cast< FatEntry * >(parent.internalPointer()) : rootEntry;
	if (!par || row < 0 || row >= par->children.size())
		return QModelIndex();
	return QAbstractItemModel::createIndex(row, column, par->children.at(row));
}

QModelIndex CustomFATFileSystemModel::parent(const QModelIndex &child) const
{
	if (!child.QModelIndex::isValid())
		return QModelIndex();
	FatEntry *node = static_cast< FatEntry * >(child.QModelIndex::internalPointer());
	FatEntry *par = node->parent;
	if (!par || par == rootEntry)
		return QModelIndex();
	FatEntry *grand = par->parent;
	int row = grand ? grand->children.indexOf(par) : 0;
	return QAbstractItemModel::createIndex(row, 0, par);
}

int CustomFATFileSystemModel::rowCount(const QModelIndex &parent) const
{
	FatEntry *node = parent.QModelIndex::isValid() ? static_cast< FatEntry * >(parent.QModelIndex::internalPointer()) : rootEntry;
	return node ? node->children.size() : 0;
}

QVariant CustomFATFileSystemModel::data(const QModelIndex &index, int role) const
{
	if (!index.isValid() || role != Qt::DisplayRole)
		return QVariant();
	FatEntry *node = static_cast< FatEntry * >(index.QModelIndex::internalPointer());
	switch (index.QModelIndex::column())
	{
	case 0:
		return node->name;
	case 1:
		return node->isDirectory ? "Dir" : formatFileSize(node->size);
	case 2:
		return node->time.QDateTime::isValid() ? node->time.toString("dd-MM-yyyy hh:mm:ss") : "N/A";
	default:
		return QVariant();
	}
}

QVariant CustomFATFileSystemModel::headerData(int section, Qt::Orientation orientation, int role) const
{
	if (orientation != Qt::Horizontal || role != Qt::DisplayRole)
		return QVariant();
	switch (section)
	{
	case 0:
		return "Name";
	case 1:
		return "Size";
	case 2:
		return "Time";
	default:
		return QVariant();
	}
}

void CustomFATFileSystemModel::buildTree(FatEntry *parentNode)
{
	QVector< FatEntry * > rawList = parser.readDirectory(parentNode->startCluster);

	for (FatEntry *node : rawList)
	{
		if (node->name == "." || node->name == "..")
		{
			continue;
		}

		node->parent = parentNode;
		parentNode->children.append(node);

		if (node->isDirectory)
		{
			buildTree(node);
		}
	}
}

void CustomFATFileSystemModel::clearTree(FatEntry *node)
{
	if (!node)
		return;
	for (FatEntry *ch : node->children)
		clearTree(ch);
	delete node;
}

QString CustomFATFileSystemModel::formatFileSize(qint64 bytes) const
{
	QString str = QString::number(bytes);
	int n = str.length() - 3;
	while (n > 0)
	{
		str.insert(n, ' ');
		n -= 3;
	}
	return str + tr(" bytes");
}
