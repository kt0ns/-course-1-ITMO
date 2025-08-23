#include "fatbrowserwindow.h"

#include <QDebug>
#include <QHeaderView>
#include <QMessageBox>
#include <QVBoxLayout>

FATBrowserWindow::FATBrowserWindow(const QString &imagePath, QWidget *parent) : QWidget(parent)
{
	initFATBrowser();
	loadImage(imagePath);
}

void FATBrowserWindow::initFATBrowser()
{
	QVBoxLayout *mainLayout = new QVBoxLayout(this);
	QHBoxLayout *controlLayout = new QHBoxLayout();

	backButton = new QPushButton(tr("Back"), this);
	backButton->setObjectName("backButtonRight");

	controlLayout->addWidget(backButton);
	mainLayout->addLayout(controlLayout);

	treeView = new QTreeView(this);
	mainLayout->addWidget(treeView);

	fatModel = new CustomFATFileSystemModel(this);

	proxyModel = new QSortFilterProxyModel(this);

	treeView->setSelectionMode(QAbstractItemView::ExtendedSelection);

	treeView->setItemsExpandable(false);
	treeView->setRootIsDecorated(false);

	treeView->sortByColumn(0, Qt::AscendingOrder);
	treeView->setSortingEnabled(true);

	connect(
		backButton,
		&QPushButton::clicked,
		this,
		[=]()
		{
			QModelIndex curProxyRoot = treeView->rootIndex();
			QModelIndex curSrcRoot = proxyModel->mapToSource(curProxyRoot);
			QModelIndex parentSrc = fatModel->parent(curSrcRoot);
			if (parentSrc.isValid())
			{
				treeView->setRootIndex(proxyModel->mapFromSource(parentSrc));
			}
		});

	connect(
		treeView,
		&QTreeView::doubleClicked,
		this,
		[=](const QModelIndex &proxyIndex)
		{
			QModelIndex srcIndex = proxyModel->mapToSource(proxyIndex);
			if (fatModel->isDir(srcIndex))
			{
				treeView->setRootIndex(proxyModel->mapFromSource(srcIndex));
			}
		});
}

void FATBrowserWindow::loadImage(const QString &imagePath)
{
	if (imagePath.isEmpty())
	{
		QMessageBox::warning(this, tr("Error"), tr("Too few command line arguments to open image"));
		return;
	}

	if (!imagePath.isEmpty() && !fatModel->setRootPath(imagePath))
	{
		QMessageBox::warning(this, tr("Error"), tr("Failed to open image: %1").arg(imagePath));
		return;
	}

	proxyModel->setSourceModel(fatModel);

	treeView->setModel(proxyModel);
	QModelIndex rootIndex = fatModel->index(0, 0, QModelIndex());
	treeView->setRootIndex(proxyModel->mapFromSource(rootIndex));
}
