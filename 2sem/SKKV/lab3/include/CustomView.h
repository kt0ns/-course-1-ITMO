#ifndef CUSTOMVIEW_H
#define CUSTOMVIEW_H

#include <QFileSystemModel>
#include <QSortFilterProxyModel>

class CustomFileSystemView : public QSortFilterProxyModel
{
  public:
	CustomFileSystemView(QObject *parent = nullptr) : QSortFilterProxyModel(parent) {}

	QVariant data(const QModelIndex &proxyIndex, int role) const override
	{
		if (role == Qt::DisplayRole)
		{
			QModelIndex sourceIndex = mapToSource(proxyIndex);
			auto *fsModel = static_cast< QFileSystemModel * >(sourceModel());
			QFileInfo fi = fsModel->fileInfo(sourceIndex);

			switch (proxyIndex.column())
			{
			case 0:
				return fsModel->fileName(sourceIndex);
			case 1:
				return fi.isDir() ? "Dir" : formatFileSize(fi.size());
			}
		}
		return QSortFilterProxyModel::data(proxyIndex, role);
	}

	QVariant headerData(int section, Qt::Orientation orientation, int role) const override
	{
		if (role == Qt::DisplayRole && orientation == Qt::Horizontal)
		{
			switch (section)
			{
			case 3:
				return "Time";
			}
		}
		return QSortFilterProxyModel::headerData(section, orientation, role);
	}

  private:
	QString formatFileSize(qint64 bytes) const
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
};

#endif
