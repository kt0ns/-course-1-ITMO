#ifndef FATENTRY_H
#define FATENTRY_H

#include <QDateTime>
#include <QString>
#include <QVector>

struct FatEntry
{
	QString name;
	quint32 size;
	QDateTime time;

	bool isDirectory;

	quint16 startCluster;
	quint64 dataOffset;
	quint32 clusterCount;

	FatEntry *parent = nullptr;
	QVector< FatEntry * > children;
};

#endif	  // FATENTRY_H
