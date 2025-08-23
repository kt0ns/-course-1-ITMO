# Template.
TARGET = fatmanager
TEMPLATE = app

# Configs.
CONFIG += c++latest
CONFIG += strict_c++
CONFIG += qt

# Qt modules.
QT += core widgets gui

INCLUDEPATH += .
INCLUDEPATH += include/

HEADERS += \
    include/CustomFATFileSystemModel.h \
    include/CustomView.h \
    include/FATBrowserWindow.h \
    include/FATEntry.h \
    include/FATParser.h \
    include/fileBrowserWindow.h \
    include/mainwindow.h

SOURCES += \
    src/CustomFATFileSystemModel.cpp \
    src/FATBrowserWindow.cpp \
    src/FATParser.cpp \
    src/fileBrowserWindow.cpp \
    src/main.cpp \
    src/mainwindow.cpp

# Add resources as needed.
# File 'resources.qrc' is expected in the repo's root.


TRANSLATIONS += \
    app_ru.ts \
    app_en.ts

RESOURCES += \
    translations.qrc
