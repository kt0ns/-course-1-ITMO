#ifndef CUSTOMFATFILESYSTEMMODEL_H
#define CUSTOMFATFILESYSTEMMODEL_H

#include "FATParser.h"

#include <QAbstractItemModel>
#include <QDir>
#include <QString>
#include <QVector>
class CustomFATFileSystemModel : public QAbstractItemModel
{
	Q_OBJECT

  public:
	explicit CustomFATFileSystemModel(QObject* parent = nullptr);

	bool setRootPath(const QString& imagePath);
	bool isDir(const QModelIndex& index) const;

	QModelIndex index(int row, int column, const QModelIndex& parent = QModelIndex()) const override;
	QModelIndex parent(const QModelIndex& child) const override;
	int rowCount(const QModelIndex& parent = QModelIndex()) const override;
	int columnCount(const QModelIndex& parent = QModelIndex()) const override
	{
		Q_UNUSED(parent);
		return 3;
	};

	QVariant data(const QModelIndex& index, int role = Qt::DisplayRole) const override;
	QVariant headerData(int section, Qt::Orientation orientation, int role = Qt::DisplayRole) const override;

  private:
	FATParser parser;
	FatEntry* rootEntry = nullptr;

	void buildTree(FatEntry* parentNode);
	void clearTree(FatEntry* node);
	QString formatFileSize(qint64 bytes) const;
};

#endif	  // CUSTOMFATFILESYSTEMMODEL_H
