cmake_minimum_required(VERSION 3.30)
project(lab2 LANGUAGES C)

set(CMAKE_C_STANDARD 17)
set(CMAKE_WINDOWS_EXPORT_ALL_SYMBOLS ON)

add_compile_definitions(_CRT_SECURE_NO_WARNINGS)
add_compile_options(-fms-runtime-lib=dll)

add_library(
        RBTree SHARED
        RBTree.c
        RBtree.h
        utils.h
        utils.c
)

include_directories(D:/cpp/graphviz/include)
link_directories(D:/cpp/graphviz/lib)

add_executable(lab2
        main.c
        dot_worker.h
        dot_worker.c
)

target_link_libraries(lab2 RBTree cgraph)