#include "fileBrowserWindow.h"

#include "CustomView.h"

#include <QDebug>
#include <QHBoxLayout>
#include <QHeaderView>
#include <QLineEdit>
#include <QPushButton>
#include <QTreeView>
#include <QVBoxLayout>

FileBrowserWindow::FileBrowserWindow(QWidget *parent) : QWidget(parent)
{
	qDebug() << "left path:" << QDir::homePath();
	initFileBrowser(QDir::currentPath());
}

void FileBrowserWindow::initFileBrowser(const QString &initialPath)
{
	QVBoxLayout *mainLayout = new QVBoxLayout(this);

	QHBoxLayout *controlLayout = new QHBoxLayout();
	QPushButton *backButton = new QPushButton(tr("Back"), this);

	backButton->setObjectName("backButtonLeft");

	controlLayout->addWidget(backButton);
	mainLayout->addLayout(controlLayout);

	QFileSystemModel *model = new QFileSystemModel(this);
	model->setRootPath(initialPath);
	model->setFilter(QDir::AllEntries | QDir::NoDotAndDotDot);

	CustomFileSystemView *proxyModel = new CustomFileSystemView(this);
	proxyModel->setSourceModel(model);

	treeView = new QTreeView(this);
	treeView->setModel(proxyModel);

	QModelIndex srcRoot = model->index(initialPath);
	treeView->setRootIndex(proxyModel->mapFromSource(srcRoot));
	treeView->setSelectionMode(QAbstractItemView::ExtendedSelection);

	treeView->setItemsExpandable(false);
	treeView->setRootIsDecorated(false);

	treeView->setColumnHidden(2, true);

	treeView->sortByColumn(0, Qt::AscendingOrder);
	treeView->setSortingEnabled(true);

	mainLayout->addWidget(treeView);
	setLayout(mainLayout);

	connect(
		treeView,
		&QTreeView::doubleClicked,
		this,
		[=](const QModelIndex &proxyIndex)
		{
			QModelIndex srcIndex = proxyModel->mapToSource(proxyIndex);
			if (model->isDir(srcIndex))
			{
				QModelIndex newProxyRoot = proxyModel->mapFromSource(srcIndex);
				treeView->setRootIndex(newProxyRoot);
			}
		});

	connect(
		backButton,
		&QPushButton::clicked,
		this,
		[=]()
		{

#ifdef Q_OS_WIN
			QModelIndex drivesSrc = model->index(QString());
			QModelIndex drivesProxy = proxyModel->mapFromSource(drivesSrc);
			if (treeView->rootIndex() == drivesProxy)
				return;
#endif

			QModelIndex srcRoot = proxyModel->mapToSource(treeView->rootIndex());
			QModelIndex parentSrc = srcRoot.parent();

			if (!parentSrc.isValid())
			{
#ifdef Q_OS_WIN
				treeView->setRootIndex(drivesProxy);
#endif
				return;
			}

			QModelIndex newProxy = proxyModel->mapFromSource(parentSrc);
			treeView->setRootIndex(newProxy);
		});
}
