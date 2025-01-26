# Импорт необходимых библиотекк для работы с числами, построения графиков и их стилизации
import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.axes_grid1.inset_locator import inset_axes
import scienceplots  # Подключение стиля для визуализации

# Увеличиваем размер графика для улучшения визуализации
plt.figure(figsize=(15, 8))  # Устанавливаем размеры графика

# Устанавливаем научный стиль графика
plt.style.use(['science', 'notebook'])

# Определение параметров для последовательности
n = np.arange(1, 101)  # Создаем массив из первых 100 положительных чисел (от 1 до 100)
sequence = (3 * n + 5) / (6 * n - 2) * np.arcsin(np.sqrt((2 + (-1) ** n) / 4))  # Формула последовательности

# Определение супремума, инфимума, верхнего и нижнего пределов для всей последовательности
supremum = np.pi * 11 / 30
infimum = np.pi / 12
upper_limit = np.pi / 6
lower_limit = np.pi / 12
offset = 5  # Смещение текста от края графика


def showSequence():
    # Построение основного графика последовательности
    plt.plot(n, sequence, label='Последовательность', color='b', marker='o', markersize=3, linestyle='--',
             linewidth=0.8)


def showSup():
    plt.axhline(supremum, color='g', linestyle='--', label='Супремум')  # Горизонтальная линия для супремума
    plt.text(100 + offset, supremum, f'{supremum:.2f}', color='g', va='center',
             ha='left')  # Подписи значений пределов на оси y с небольшим сдвигом вправо


def showInf():
    plt.axhline(infimum, color='r', linestyle='--', label='Инфимум')  # Горизонтальная линия для инфимума
    plt.text(100 + offset, infimum, f'{infimum:.2f}', color='r', va='center',
             ha='left')  # Подписи значений пределов на оси y с небольшим сдвигом вправо


def showUpperLim():
    plt.axhline(upper_limit, color='purple', linestyle=':', label='Верхний предел')  # Линия для верхнего предела
    plt.text(100 + offset, upper_limit, f'{upper_limit:.2f}', color='purple', va='center',
             ha='left')  # Подписи значений пределов на оси y с небольшим сдвигом вправо


def showLowerLim():
    plt.axhline(lower_limit, color='orange', linestyle=':', label='Нижний предел')  # Линия для нижнего предела
    plt.text(100 + offset, lower_limit, f'{lower_limit:.2f}', color='orange', va='center',
             ha='left')  # Подписи значений пределов на оси y с небольшим сдвигом вправо


def showLegend():
    # Настройки графика
    plt.xlabel('n')  # Подпись оси X
    plt.ylabel('Значение последовательности')  # Подпись оси Y
    plt.title('График последовательности с ε-окрестностью и предельными значениями')  # Заголовок графика

    # Перемещаем легенду вниз и влево для лучшей читаемости
    plt.legend(loc='upper left', bbox_to_anchor=(0.77, 0.9), borderaxespad=0., frameon=True, shadow=True, fancybox=True,
               fontsize='small')


def showTask1():
    showSequence()
    showSup()
    showInf()
    showUpperLim()
    showLowerLim()
    showLegend()

def showTask2():
    # Создание подпоследовательности x(2k)
    even_indices = n[1::2]  # Индексы, соответствующие четным значениям n
    even_sequence = sequence[1::2]  # Значения последовательности на этих индексах
    # Добавляем фиолетовые маркеры для четных точек последовательности
    plt.scatter(even_indices, even_sequence, color='purple', label='Подпоследовательность x(2k)', s=10, zorder=3)


def showTask3(e):
    # Задаем ε-окрестность 0.01 и находим первую точку в подпоследовательности, попадающую в эту окрестность
    epsilon = e
    limit_value = np.pi / 6  # Определяем предел

    # Ищем n0, начиная с 1
    n0 = None
    for i in range(1, 1_000_000):
        tmp = (3 * i + 5) / (6 * i - 2) * np.arcsin(
            np.sqrt((2 + (-1) ** i) / 4))  # Вычисляем i-й элемент последовательности
        if abs(tmp - limit_value) < epsilon:  # Проверяем, чтобы значение было в ε-окрестности
            n0 = i  # Сохраняем индекс
            break  # Останавливаем поиск после нахождения первой подходящей точки

    # Четные индексы, начиная с n0
    even_indices = np.arange(n0, n0 + 200, 2)  # Четные индексы от n0
    even_sequence = (3 * even_indices + 5) / (6 * even_indices - 2) * np.arcsin(
        np.sqrt((2 + (-1) ** even_indices) / 4))  # Значения подпоследовательности

    # Увеличиваем размер графика для улучшения визуализации
    plt.figure(figsize=(15, 8))  # Устанавливаем размеры графика

    # Устанавливаем научный стиль графика
    plt.style.use(['science', 'notebook'])

    # Добавляем линию для предела подпоследовательности
    plt.axhline(limit_value, color='black', linestyle='-.', label='Предел подпоследовательности')

    # Построение графика четной подпоследовательности
    plt.plot(even_indices, even_sequence, label='Четная подпоследовательность', color='b', marker='o', markersize=3,
             linestyle='--', linewidth=0.8)

    # Построение ε-окрестности вокруг предельного значения (зелёный полупрозрачный фон)
    plt.fill_between(even_indices, limit_value - epsilon, limit_value + epsilon, color='green', alpha=0.2,
                     label=fr'$\epsilon$-окрестность ({epsilon})')

    # Выделяем первую точку подпоследовательности в ε-окрестности
    first_in_epsilon_index = np.where(np.abs(even_sequence - limit_value) < epsilon)[0]
    if first_in_epsilon_index.size > 0:
        plt.scatter(even_indices[first_in_epsilon_index[0]], even_sequence[first_in_epsilon_index[0]], color='red',
                    s=90, zorder=4, label='Первая точка в окрестности')

    # Добавление значения предела справа от линии
    plt.text(even_indices[-1] + 12, limit_value, f'{limit_value:.4f}', fontsize=12, color='black', va='center')

    # Настройка легенды
    plt.legend(loc='upper left', bbox_to_anchor=(1.02, 0.9), borderaxespad=0., frameon=True, shadow=True, fancybox=True,
               fontsize='small')

    plt.grid(True)  # Основная сетка
    plt.xlabel('Индекс n')
    plt.ylabel('Значение последовательности')
    plt.title('График четной подпоследовательности и ε-окрестности')

    plt.tight_layout()  # Учитываем дополнительные элементы графика для компактного расположения
    plt.show()  # Отображаем график

def showTast4(e):
    # Ищем первую точку, значение которой больше инфимума на ε
    eps = e  # Значение ε
    x_m = 0  # Хранит найденное значение
    m = 0  # Хранит индекс найденного значения
    for i in range(1, 1_000_000):
        tmp = (3 * i + 5) / (6 * i - 2) * np.arcsin(
            np.sqrt((2 + (-1) ** i) / 4))  # Вычисляем i-й элемент последовательности
        if tmp < lower_limit + eps:  # Проверяем, чтобы значение было больше инфимума на ε
            x_m = tmp  # Сохраняем значение
            m = i  # Сохраняем индекс
            print(f"Найденное значение меньшее инфимума: {x_m}")
            print(f"Номер: {i}")
            break  # Останавливаем поиск после нахождения первой подходящей точки

    # Выделяем точку, которая больше инфимума на ε
    if m in n:
        plt.scatter(m, x_m, color='magenta', s=30, zorder=5, label='Точка > inf + eps')

    # Вставка увеличенного участка графика в виде дополнительного окна
    ax_inset = inset_axes(plt.gca(), width="40%", height="30%", loc="upper center")
    ax_inset.plot(np.arange(m - 10, m + 10),
                  [(3 * i + 5) / (6 * i - 2) * np.arcsin(np.sqrt((2 + (-1) ** i) / 4)) for i in
                   np.arange(m - 10, m + 10)], label='Последовательность', color='b', marker='o', markersize=3,
                  linestyle='--',
                  linewidth=0.8)
    ax_inset.plot(n, sequence, color='b', marker='o', markersize=3, linestyle='--',
                  linewidth=0.8)  # График последовательности
    ax_inset.axhline(infimum, color='r', linestyle='--')  # Линия инфимума
    ax_inset.axhline(lower_limit, color='orange', linestyle=':')  # Линия нижнего предела
    ax_inset.scatter(m, x_m, color='magenta', s=10, zorder=5)  # Маркер для точки больше инфимума на ε

    # Окрашиваем ε-окрестность инфимума зеленым цветом с прозрачностью
    ax_inset.fill_between(np.arange(m - 10, m + 10), infimum - eps, infimum + eps, color='green', alpha=0.2,
                          label=fr'$\epsilon$-окрестность ({eps})')

    # Задаем границы увеличенной области
    ax_inset.set_xlim(m - m % 10, m - m % 10 + 10)  # Область по оси X
    ax_inset.set_ylim(x_m - eps * 1.4, x_m + eps * 1.4)  # Область значений по оси Y
    ax_inset.grid(True)  # Включаем сетку для лучшей читаемости


showTask1()
showTask2()
showTast4(0.01)

showTask3(0.001)
