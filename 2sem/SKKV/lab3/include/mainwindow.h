#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include "fatbrowserwindow.h"
#include "fileBrowserWindow.h"

#include <QEvent>
#include <QFileSystemModel>
#include <QLabel>
#include <QMainWindow>
#include <QSettings>
#include <QTranslator>
#include <QTreeView>

class QAction;

class MainWindow : public QMainWindow
{
	Q_OBJECT

  public:
	MainWindow(char **argv);

  public slots:

	void configureMountShortcut();
	void configureAboutShortcut();
	void configureCopyShortcut();
	void configureSizeShortcut();
	void configureExitShortcut();
	void configureMountDShortcut();
	void configureDefaultShortcut();

	void sizeButton();
	void copyButton();
	void mountButton();
	void mountDButton();
	void aboutButton();

  signals:

  private:
	QLabel *statusLabel;
	QMenu *langMenu, *configMenu;
	FileBrowserWindow *leftWindow;
	FATBrowserWindow *rightWindow;

	QAction *actAbout, *actMount, *actMountD, *actCopy, *actExit, *actSize;

	QTranslator trans;
	QSettings s;

	void closeEvent(QCloseEvent *event) override;

	void updateTranslations();
	void switchLanguage(const QString &langCode);
	void loadShortcuts();
	void setDefaultShortcuts();
	void initializationToolbar();
	void initializationMenu();
	QString formatSize(quint64 bytes) const;
	quint64 dirSizeQFS(const QString &path) const;
	quint64 dirSizeFAT(FatEntry *node) const;
};

#endif	  // MAINWINDOW_H
