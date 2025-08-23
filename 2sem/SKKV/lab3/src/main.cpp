#include "mainwindow.h"

#include <QApplication>

int main(int argc, char **argv)
{
	QApplication app(argc, argv);

	QCoreApplication::setOrganizationName("FATManager");
	QSettings::setDefaultFormat(QSettings::IniFormat);
	QSettings::setPath(QSettings::IniFormat, QSettings::UserScope, QCoreApplication::applicationDirPath());

	MainWindow window(argv);
	window.show();

	return app.exec();
}
