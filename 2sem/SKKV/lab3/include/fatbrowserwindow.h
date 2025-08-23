#ifndef FATBROWSERWINDOW_H
#define FATBROWSERWINDOW_H

#include "CustomFATFileSystemModel.h"

#include <QLineEdit>
#include <QPushButton>
#include <QSortFilterProxyModel>
#include <QSplitter>
#include <QTreeView>
#include <QWidget>

class FATBrowserWindow : public QWidget
{
	Q_OBJECT

  public:
	FATBrowserWindow(const QString &imagePath, QWidget *parent = nullptr);
	void loadImage(const QString &imagePath);

  private:
	void initFATBrowser();

	QSplitter *splitter;
	QLineEdit *pathLineEdit;
	QPushButton *backButton;
	QTreeView *treeView;
	CustomFATFileSystemModel *fatModel;
	QSortFilterProxyModel *proxyModel;
};

#endif	  // FATBROWSERWINDOW_H
