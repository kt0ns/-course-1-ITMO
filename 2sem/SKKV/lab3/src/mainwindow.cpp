#include "mainwindow.h"

#include "CustomView.h"
#include "fatbrowserwindow.h"

#include <QApplication>
#include <QCloseEvent>
#include <QDialogButtonBox>
#include <QDir>
#include <QFormLayout>
#include <QInputDialog>
#include <QKeySequenceEdit>
#include <QMainWindow>
#include <QMenu>
#include <QMenuBar>
#include <QMessageBox>
#include <QSplitter>
#include <QStatusBar>
#include <QToolBar>
#include <QVBoxLayout>

MainWindow::MainWindow(char **argv) :
	QMainWindow(), leftWindow(new FileBrowserWindow(this)),
	rightWindow(new FATBrowserWindow(QString::fromLocal8Bit(argv[1]), this))
{
	setWindowTitle("FAT Manager");

	QSplitter *splitter = new QSplitter(Qt::Horizontal, this);

	splitter->addWidget(leftWindow);
	splitter->addWidget(rightWindow);

	setCentralWidget(splitter);

	this->statusBar()->show();

	statusLabel = new QLabel(tr("READY"));
	statusLabel->setObjectName("statusLabel");
	QWidget *widget = new QWidget(this);
	widget->setFixedWidth(50);
	widget->setHidden(false);
	this->statusBar()->addWidget(widget, 1);
	this->statusBar()->addWidget(statusLabel, 1);

	initializationMenu();

	initializationToolbar();

	setDefaultShortcuts();

	loadShortcuts();

	QSettings settings;
	QString langCode = settings.value("language", "ru").toString();

	switchLanguage(langCode);

	resize(1600, 1000);
}

void MainWindow::closeEvent(QCloseEvent *event)
{
	QMessageBox::StandardButton resBtn = QMessageBox::question(
		this,
		tr("Close the program"),
		tr("Are you sure you want to close the program?"),
		QMessageBox::No | QMessageBox::Yes,
		QMessageBox::Yes);

	if (resBtn == QMessageBox::Yes)
	{
		event->accept();
	}
	else
	{
		event->ignore();
	}
}

void MainWindow::aboutButton()
{
	QMessageBox::information(
		this,
		tr("About Program"),
		tr("The program provides tools for working with the file system structure.\n\n"
		   "▶ Left panel:\n"
		   " - navigation through directories and drives of the device's file system\n"
		   " - displaying file creation date and size\n\n"
		   "▶ Right panel:\n"
		   " - displays the contents of file system images in FAT16 and FAT32 formats\n\n"
		   "▶ Additional features:\n"
		   " - sorting by key columns\n"
		   " - functional buttons:\n"
		   "    • About — displays this message\n"
		   "    • Mount — opens the selected file as a file system in the right panel\n"
		   "    • MountD — opens a file by path as a file system in the right panel\n"
		   "    • Size — shows the size of the selected directories or files in a popup\n"
		   "    • Exit — safely exits the application\n\n"
		   "▶ The top menu also provides options to:\n"
		   " - change the interface language\n"
		   " - assign custom key bindings\n"));
}

void MainWindow::copyButton()
{
	QMessageBox::information(this, tr("Warning"), tr("Not implemented"));
}

void MainWindow::mountDButton()
{
	QString path = QInputDialog::getText(this, tr("Enter path"), tr("Path to the image:"), QLineEdit::Normal, QString());

	rightWindow->loadImage(path);
}

void MainWindow::mountButton()
{
	QTreeView *view = leftWindow->findChild< QTreeView * >();
	if (!view)
		return;

	QTreeView *right = rightWindow->findChild< QTreeView * >();
	if (right->hasFocus())
		return;

	const auto rows = view->selectionModel()->selectedRows(0);

	if (rows.empty())
	{
		QMessageBox::warning(this, tr("Warning"), tr("Nothing is selected"));
		return;
	}
	else if (rows.size() > 1)
	{
		QMessageBox::warning(this, tr("Error"), tr("Too many files selected"));
		return;
	}

	QModelIndex idx = rows.first();
	if (!idx.isValid())
		return;

	QString path = view->model()->data(idx, QFileSystemModel::FilePathRole).toString();
	QFileInfo info(path);

	if (info.isDir())
	{
		return;
	}

	rightWindow->loadImage(path);
}

void MainWindow::sizeButton()
{
	QTreeView *left = leftWindow->findChild< QTreeView * >();
	QTreeView *right = rightWindow->findChild< QTreeView * >();
	if (!left)
		return;

	QTreeView *view = left->hasFocus() ? left : right;

	QModelIndexList rows;
	if (view->hasFocus())
	{
		rows = view->selectionModel()->selectedRows(0);
	}

	if (rows.isEmpty())
	{
		QPoint pt = view->viewport()->mapFromGlobal(QCursor::pos());
		QModelIndex idxUnderCursor = view->indexAt(pt);
		if (idxUnderCursor.isValid())
		{
			rows << idxUnderCursor;
		}
	}

	if (rows.isEmpty())
	{
		QMessageBox::warning(this, tr("Warning"), tr("Nothing is selected and the cursor is not pointing to an element"));
		return;
	}

	QAbstractItemModel *model = view->model();
	auto proxy = qobject_cast< QAbstractProxyModel * >(model);
	QAbstractItemModel *srcModel = proxy ? proxy->sourceModel() : model;

	if (auto fsModel = qobject_cast< QFileSystemModel * >(srcModel))
	{
		for (const QModelIndex &idxProxy : rows)
		{
			QModelIndex idx = proxy ? proxy->mapToSource(idxProxy) : idxProxy;
			if (!fsModel->isDir(idx))
				continue;

			QString path = fsModel->filePath(idx);
			quint64 sz = dirSizeQFS(path);
			QMessageBox::information(this, tr("Size of %1").arg(path), tr("Current size: %1").arg(formatSize(sz)));
		}
	}
	else if (auto fatModel = qobject_cast< CustomFATFileSystemModel * >(srcModel))
	{
		for (const QModelIndex &idxProxy : rows)
		{
			QModelIndex idx = proxy ? proxy->mapToSource(idxProxy) : idxProxy;
			auto node = static_cast< FatEntry * >(idx.internalPointer());
			if (!node || !node->isDirectory)
				continue;

			QMessageBox::information(this, tr("Size of %1").arg(node->name), tr("Current size: %1").arg(formatSize(dirSizeFAT(node))));
		}
	}
}

quint64 MainWindow::dirSizeQFS(const QString &path) const
{
	quint64 total = 0;
	QDir dir(path);
	for (auto &fi : dir.entryInfoList(QDir::Files))
		total += fi.size();
	for (auto &sub : dir.entryList(QDir::Dirs | QDir::NoDotAndDotDot))
		total += dirSizeQFS(dir.filePath(sub));
	return total;
}

quint64 MainWindow::dirSizeFAT(FatEntry *node) const
{
	quint64 total = 0;
	for (FatEntry *ch : node->children)
	{
		if (ch->isDirectory)
			total += dirSizeFAT(ch);
		else
			total += ch->size;
	}
	return total;
}

QString MainWindow::formatSize(quint64 bytes) const
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

void MainWindow::updateTranslations()
{
	statusLabel->setText(tr("READY"));

	if (auto btn = leftWindow->findChild< QPushButton * >("backButtonLeft"))
		btn->setText(tr("Back"));

	if (auto btn = rightWindow->findChild< QPushButton * >("backButtonRight"))
		btn->setText(tr("Back"));

	actAbout->setText(tr("About"));
	actMount->setText(tr("Mount"));
	actMountD->setText(tr("MountD"));
	actSize->setText(tr("Size"));
	actCopy->setText(tr("Copy"));
	actExit->setText(tr("Exit"));

	actAbout->setStatusTip(tr("About"));
	actMount->setStatusTip(tr("Mount"));
	actMountD->setStatusTip(tr("MountD"));
	actSize->setStatusTip(tr("Size"));
	actCopy->setStatusTip(tr("Copy"));
	actExit->setStatusTip(tr("Exit"));

	langMenu->setTitle(tr("Language"));
	configMenu->setTitle(tr("Сhange default key combination"));
}

void MainWindow::switchLanguage(const QString &langCode)
{
	qApp->removeTranslator(&trans);

	trans.load(":/translations/app_" + langCode + ".qm");
	qApp->installTranslator(&trans);

	updateTranslations();

	QSettings().setValue("language", langCode);
}

static bool runShortcutDialog(QWidget *parent, const QString &title, const QString &label, QKeySequence &outSequence)
{
	QDialog dlg(parent);
	dlg.setWindowTitle(title);

	QFormLayout form(&dlg);
	auto *edit = new QKeySequenceEdit(outSequence, &dlg);
	form.addRow(label, edit);

	QDialogButtonBox buttons(QDialogButtonBox::Ok | QDialogButtonBox::Cancel, Qt::Horizontal, &dlg);
	form.addRow(&buttons);

	QObject::connect(&buttons, &QDialogButtonBox::accepted, &dlg, &QDialog::accept);
	QObject::connect(&buttons, &QDialogButtonBox::rejected, &dlg, &QDialog::reject);

	if (dlg.exec() == QDialog::Accepted)
	{
		outSequence = edit->keySequence();
		return true;
	}
	return false;
}

void MainWindow::configureMountDShortcut()
{
	QKeySequence seq = actMountD->shortcut();
	if (runShortcutDialog(this, tr("Configure Mount Shortcut"), tr("Press new shortcut for Mount:"), seq))
	{
		actMountD->setShortcut(seq);
		s.setValue("shortcuts/mount", seq.toString());
	}
}

void MainWindow::configureMountShortcut()
{
	QKeySequence seq = actMount->shortcut();
	if (runShortcutDialog(this, tr("Configure Mount Shortcut"), tr("Press new shortcut for Mount:"), seq))
	{
		actMount->setShortcut(seq);
		s.setValue("shortcuts/mount", seq.toString());
	}
}

void MainWindow::configureAboutShortcut()
{
	QKeySequence seq = actAbout->shortcut();
	if (runShortcutDialog(this, tr("Configure About Shortcut"), tr("Press new shortcut for About:"), seq))
	{
		actAbout->setShortcut(seq);
		s.setValue("shortcuts/about", seq.toString());
	}
}

void MainWindow::configureCopyShortcut()
{
	QKeySequence seq = actCopy->shortcut();
	if (runShortcutDialog(this, tr("Configure Copy Shortcut"), tr("Press new shortcut for Copy:"), seq))
	{
		actCopy->setShortcut(seq);
		s.setValue("shortcuts/copy", seq.toString());
	}
}

void MainWindow::configureSizeShortcut()
{
	QKeySequence seq = actSize->shortcut();
	if (runShortcutDialog(this, tr("Configure Size Shortcut"), tr("Press new shortcut for Size:"), seq))
	{
		actSize->setShortcut(seq);
		s.setValue("shortcuts/size", seq.toString());
	}
}

void MainWindow::configureExitShortcut()
{
	QKeySequence seq = actExit->shortcut();
	if (runShortcutDialog(this, tr("Configure Exit Shortcut"), tr("Press new shortcut for Exit:"), seq))
	{
		actExit->setShortcut(seq);
		s.setValue("shortcuts/exit", seq.toString());
	}
}

void MainWindow::configureDefaultShortcut()
{
	s.remove("shortcuts/");

	setDefaultShortcuts();
}

void MainWindow::loadShortcuts()
{
	QString t;

	t = s.value("shortcuts/mountD").toString();
	if (!t.isEmpty())
		actMountD->setShortcut(QKeySequence(t));

	t = s.value("shortcuts/mount").toString();
	if (!t.isEmpty())
		actMount->setShortcut(QKeySequence(t));

	t = s.value("shortcuts/about").toString();
	if (!t.isEmpty())
		actAbout->setShortcut(QKeySequence(t));

	t = s.value("shortcuts/copy").toString();
	if (!t.isEmpty())
		actCopy->setShortcut(QKeySequence(t));

	t = s.value("shortcuts/size").toString();
	if (!t.isEmpty())
		actSize->setShortcut(QKeySequence(t));

	t = s.value("shortcuts/exit").toString();
	if (!t.isEmpty())
		actExit->setShortcut(QKeySequence(t));
}

void MainWindow::setDefaultShortcuts()
{
	actAbout->setShortcut(QKeySequence(Qt::Key_F1));
	actMount->setShortcut(QKeySequence(Qt::Key_F2));
	actSize->setShortcut(QKeySequence(Qt::Key_F3));
	actMountD->setShortcut(QKeySequence(Qt::Key_F4));
	actCopy->setShortcut(QKeySequence(Qt::Key_F5));
	actExit->setShortcut(QKeySequence(Qt::Key_F10));
}

void MainWindow::initializationToolbar()
{
	actAbout = new QAction(style()->standardIcon(QStyle::SP_DialogHelpButton), tr("&About"), this);
	actAbout->setStatusTip("About");
	connect(actAbout, &QAction::triggered, this, &MainWindow::aboutButton);

	actMount = new QAction(style()->standardIcon(QStyle::SP_DriveFDIcon), tr("&Mount"), this);
	actMount->setStatusTip(tr("Mount"));
	connect(actMount, &QAction::triggered, this, &MainWindow::mountButton);

	actMountD = new QAction(style()->standardIcon(QStyle::SP_DialogOpenButton), tr("&Mount..."), this);
	actMountD->setStatusTip(tr("Mount..."));
	connect(actMountD, &QAction::triggered, this, &MainWindow::mountDButton);

	actSize = new QAction(style()->standardIcon(QStyle::SP_FileDialogDetailedView), tr("Size"), this);
	actSize->setStatusTip(tr("Size"));
	connect(actSize, &QAction::triggered, this, &MainWindow::sizeButton);

	actCopy = new QAction(style()->standardIcon(QStyle::SP_FileDialogContentsView), tr("&Copy"), this);
	actCopy->setStatusTip("Copy");
	connect(actCopy, &QAction::triggered, this, &MainWindow::copyButton);

	actExit = new QAction(style()->standardIcon(QStyle::SP_DialogCloseButton), tr("&Exit"), this);
	actExit->setStatusTip(tr("Exit"));
	connect(actExit, &QAction::triggered, this, &QWidget::close);

	QToolBar *bottomToolbar = new QToolBar("Toolbar", this);
	addToolBar(Qt::BottomToolBarArea, bottomToolbar);

	bottomToolbar->addAction(actAbout);
	bottomToolbar->addAction(actMount);
	bottomToolbar->addAction(actMountD);
	bottomToolbar->addAction(actSize);
	bottomToolbar->addAction(actCopy);
	bottomToolbar->addAction(actExit);
}

void MainWindow::initializationMenu()
{
	langMenu = menuBar()->addMenu(tr("Language"));
	langMenu->addAction(tr("Русский"), this, [this]() { switchLanguage("ru"); });
	langMenu->addAction(tr("English"), this, [this]() { switchLanguage("en"); });

	configMenu = menuBar()->addMenu(tr("Сhange default key combination"));
	configMenu->addAction(tr("About"), this, &MainWindow::configureAboutShortcut);
	configMenu->addAction(tr("Mount"), this, &MainWindow::configureMountShortcut);
	configMenu->addAction(tr("MountD"), this, &MainWindow::configureMountDShortcut);
	configMenu->addAction(tr("Copy"), this, &MainWindow::configureCopyShortcut);
	configMenu->addAction(tr("Size"), this, &MainWindow::configureSizeShortcut);
	configMenu->addAction(tr("Exit"), this, &MainWindow::configureExitShortcut);
	configMenu->addAction(tr("Return to default"), this, &MainWindow::configureDefaultShortcut);
}
