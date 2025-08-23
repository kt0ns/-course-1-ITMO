import matplotlib.pyplot as plt
import numpy as np


def f(x):
    return x * np.log(2) - 3 ** x


def f2(x):
    return x ** 2


def function(x):
    return f(x)


def get_fineness():
    return int(input("Введите мелкость разбиения: "))


def get_equipment(N, left, right, equip_type):
    points = np.linspace(left, right, N + 1)
    intervals = np.vstack((points[:-1], points[1:]))

    if equip_type == 1:
        values = right_intervals(intervals)
    elif equip_type == 3:
        values = left_intervals(intervals)
    elif equip_type == 2:
        values = center_intervals(intervals)
    elif equip_type == 4:
        values = random_intervals(intervals)
    elif equip_type == 5:
        values = max_intervals(intervals)
    elif equip_type == 6:
        values = min_intervals(intervals)
    elif equip_type == 7:
        values = intervals
    else:
        raise ValueError("Недопустимый тип оснащения")

    return values, points


def random_intervals(intervals):
    return np.random.uniform(intervals[0, :], intervals[1, :])


def center_intervals(intervals):
    return (intervals[0, :] + intervals[1, :]) / 2


def left_intervals(intervals):
    return intervals[0, :]


def right_intervals(intervals):
    return intervals[1, :]


def max_intervals(intervals):
    max_indices = np.argmax(np.abs(intervals), axis=0)
    return intervals[max_indices, np.arange(intervals.shape[1])]


def min_intervals(intervals):
    min_indices = np.argmin(np.abs(intervals), axis=0)
    return intervals[min_indices, np.arange(intervals.shape[1])]


def get_range():
    return list(map(int, input("Введите желаемый диапазон рассмотрения функции: ").split()))[:2]


def get_sums_of_partitions(N, left, right, equip_type):
    delta = (right - left) / N
    fineness, points = get_equipment(N, left, right, equip_type)
    if equip_type == 7:
        sum_val = np.sum((function(fineness[0, :]) + function(fineness[1, :])) / 2 * delta)
    else:
        sum_val = np.sum(function(fineness) * delta)
    return sum_val, fineness, points


def plot_functions(left, right, N):
    types = [1, 2, 3, 4]
    titles = ["Правое оснащение", "Центральное оснащение", "Левое оснащение", "Случайное оснащение"]
    x = np.linspace(left, right, 500)
    y = function(x)

    fig, axes = plt.subplots(2, 2, figsize=(10, 8))
    axes = axes.ravel()

    for i, t in enumerate(types):
        sum_result, fineness, points = get_sums_of_partitions(N, left, right, t)
        colors = plt.cm.viridis(np.linspace(0, 1, N))

        for j in range(N):
            axes[i].fill_between([points[j], points[j + 1]], [function(fineness[j])] * 2, color=colors[j],
                                 edgecolor='red', linewidth=0.1)

        axes[i].plot(x, y, label="Функция f(x)", color='red', zorder=3)
        axes[i].set_title(titles[i])
        axes[i].set_xlabel("x")
        axes[i].set_ylabel("y")
        axes[i].legend()

    plt.tight_layout()
    plt.show()


def plot_min_max_figures(left, right, N):
    x = np.linspace(left, right, 500)
    y = function(x)

    sum_max, max_fineness, max_points = get_sums_of_partitions(N, left, right, 5)
    sum_min, min_fineness, min_points = get_sums_of_partitions(N, left, right, 6)

    fig, ax = plt.subplots(figsize=(8, 6))

    for j in range(N):
        ax.fill_between([min_points[j], min_points[j + 1]], [function(min_fineness[j])] * 2, color="green",
                        edgecolor='red', linewidth=0.1, alpha=1)
        ax.fill_between([max_points[j], max_points[j + 1]], [function(max_fineness[j])] * 2, color="gray",
                        edgecolor='blue', linewidth=0.1, alpha=0.5)

    ax.plot(x, y, label="Функция f(x)", color='red', zorder=3)
    ax.set_title("Верхний и нижний интеграл Дарбу")
    ax.set_xlabel("x")
    ax.set_ylabel("y")
    ax.legend()
    plt.show()


def calculate_and_print_table(fineness_values, left, right):
    types = [1, 2, 3, 4, 7]
    titles = ["Правое оснащение", "Центральное оснащение", "Левое оснащение", "Случайное оснащение", "Метод трапеций"]

    print("\nТаблица результатов:")
    print(f"{'Мелкость разбиения':<20}{'Тип оснащения':<25}{'Интегральная сумма':<20}")
    print("-" * 65)

    for N in fineness_values:
        for i, t in enumerate(types):
            sum_result, _, _ = get_sums_of_partitions(N, left, right, t)
            print(f"{N:<20}{titles[i]:<25}{sum_result:<20.6f}")
        print("-" * 65)


def trapezoidal_integral(left, right, N):
    sum_val, fineness, points = get_sums_of_partitions(N, left, right, 7)
    return sum_val


def plot_trapezoidal(left, right, N):
    sum_val, fineness, points = get_sums_of_partitions(N, left, right, 7)

    x = np.linspace(left, right, 500)
    y = function(x)

    plt.plot(x, y, label="Функция f(x)", color='b', zorder=3)

    for i in range(N):
        x_trap = [fineness[0, i], fineness[0, i], fineness[1, i], fineness[1, i]]
        y_trap = [0, function(fineness[0, i]), function(fineness[1, i]), 0]
        plt.fill(x_trap, y_trap, facecolor=(1, 0, 0, 0.1),
                 edgecolor=(1, 0, 0, 1),
                 linewidth=1)
    plt.xlabel('x')
    plt.ylabel('y')
    plt.title('Интегральная сумма методом трапеций')
    plt.legend()
    plt.show()


N = get_fineness()
left, right = get_range()
calculate_and_print_table([10, 100, 500, 1000, 100000], left, right)
plot_functions(left, right, N)
plot_min_max_figures(left, right, N)
plot_trapezoidal(left, right, N)
