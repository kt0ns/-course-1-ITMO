#ifndef FILEBROWSERWINDOW_H
#define FILEBROWSERWINDOW_H

#include <QDir>
#include <QMainWindow>
#include <QTreeView>

class QAction;

class FileBrowserWindow : public QWidget
{
	Q_OBJECT

  public:
	FileBrowserWindow(QWidget *parent = nullptr);
	~FileBrowserWindow() = default;

  private:
	void initFileBrowser(const QString &initialPath = QDir::homePath());

	QTreeView *treeView = nullptr;

  signals:

  public slots:
};

#endif	  // FILEBROWSERWINDOW_H
