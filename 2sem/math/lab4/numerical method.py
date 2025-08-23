import time

import matplotlib.pyplot as plt
import numpy as np


def f(x, y):
    return np.where((x != 0) & (y != 0), x ** 2 * y ** 2 * np.log(x ** 2 + 5 * y ** 2), 1e-3)


def dir_x(x, y):
    return 2 * x * y ** 2 * np.log(x ** 2 + 5 * y ** 2) + (2 * x ** 3 * y ** 2) / (x ** 2 + 5 * y ** 2) \
        if x != 0 and y != 0 else 1e-10


def dir_y(x, y):
    return 2 * y * x ** 2 * np.log(x ** 2 + 5 * y ** 2) + (10 * y ** 3 * x ** 2) / (x ** 2 + 5 * y ** 2) \
        if x != 0 and y != 0 else 1e-10


def grad_f(X, Y):
    return np.vstack((dir_x(X, Y), dir_y(X, Y)))


def gradient_descent(x0, y0, ak=0.01, d=1e-12, max_iter=100000):
    pt = np.array([x0, y0], dtype=float)
    history = [pt]

    last_f = f(pt[0], pt[1])

    for i in range(1, max_iter + 1):
        g = grad_f(pt[0], pt[1])
        new_pt = pt - ak * g.ravel()
        new_f = f(new_pt[0], new_pt[1])
        delta = np.linalg.norm(new_pt - pt)
        delta_f = abs(last_f - new_f)
        last_f = new_f
        history.append(new_pt.copy())
        if delta < d:
            print(f"Точка дотигнута на {i} итерации, ||(dx, dy)|| = {delta:.2e}")
            break
        if delta_f < d:
            print(f"Точка дотигнута на {i} итерации, df = {delta:.2e}")
            break
        pt = new_pt
    else:
        print("Максимальное количество итераций пройдено")
    return pt, f(pt[0], pt[1]), np.array(history)


start = (-2, -2)
ak = 0.01
print(f"Выбранная начальная точка: {start}. Значение ak (статическое) = {ak}")
s = time.time()
xmin, zmin, hist = gradient_descent(*start, ak)
execution_time = time.time() - s

real_minx = -1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4))
real_miny = -1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4))

print("Точка минимума, найденная градинентным спуском:", xmin)
print("Значение функции в найденной точке минимума:", zmin)
print(f"Реальное значение точки минимума, найденное в аналитическом методе:{real_minx:.12f}, {real_miny:.12f}")
print(f"Разница между найденным и реальным значением:{abs(xmin[0] - real_minx):.12f}, {abs(real_miny - xmin[1]):.12f}")
print(f"Время поиска градиентным спуском: {execution_time:.4f} сек")

### =========================================================================================================================
### ========================= graph =========================================================================================
### =========================================================================================================================

x = np.linspace(-3, 3, 3000)
y = np.linspace(-3, 3, 3000)
X, Y = np.meshgrid(x, y)
Z = f(X, Y)
max_z = 60

Z_clipped = np.where(Z >= max_z, np.nan, Z)

fig = plt.figure(figsize=(16, 7))

hx, hy = hist[:, 0], hist[:, 1]
hz = [f(xi, yi) for xi, yi in zip(hx, hy)]
hz_clipped = np.clip(hz, None, max_z)

ax1 = fig.add_subplot(1, 2, 1, projection='3d')
surf = ax1.plot_surface(X, Y, Z_clipped, cmap='viridis', alpha=0.7)

ax1.plot(hx, hy, hz_clipped, color='red', marker='o', markersize=3, linewidth=1, label='Путь градинетного спуска')
ax1.scatter(1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), 1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)),
            -1 / (40 * np.sqrt(np.e)), color='black', s=50, label='Локальный минимум')
ax1.scatter(1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), -1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)),
            -1 / (40 * np.sqrt(np.e)), color='black', s=50)
ax1.scatter(-1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), 1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)),
            -1 / (40 * np.sqrt(np.e)), color='black', s=50)
ax1.scatter(-1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), -1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)),
            -1 / (40 * np.sqrt(np.e)), color='black', s=50)

ax1.scatter(0, y, 0, color='gray', marker='*', s=10, alpha=1, label="Множество статических точек")
ax1.scatter(x, 0, 0, color='gray', marker='*', s=10, alpha=1)

ax1.set_title('$f(x, y) = x^2y^2ln(x^2 + 5y^2)$', fontsize=16)
ax1.set_xlabel('X')
ax1.set_ylabel('Y')
ax1.set_zlabel('f')
ax1.legend()
fig.colorbar(surf, ax=ax1, shrink=0.5, aspect=12, pad=0.1)

ax1.view_init(elev=47, azim=154)

ax2 = fig.add_subplot(1, 2, 2)
contour = ax2.contour(X, Y, Z, levels=20)

ax2.plot(hx, hy, color='red', marker='o', markersize=2, linewidth=1, label='Путь градинетного спуска')
ax2.scatter(1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), 1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)), color='black',
            s=50, label='Локальный минимум')
ax2.scatter(1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), -1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)), color='black',
            s=50)
ax2.scatter(-1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), 1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)), color='black',
            s=50)
ax2.scatter(-1 / (np.pow(2, 1 / 2) * np.pow(np.e, 1 / 4)), -1 / (np.pow(10, 1 / 2) * np.pow(np.e, 1 / 4)),
            color='black',
            s=50)

ax2.clabel(contour, inline=True, fontsize=8)
ax2.set_title('Линии уровня для $f(x, y) = x^2y^2ln(x^2 + 5y^2)$', fontsize=16)
ax2.set_xlabel('X')
ax2.set_ylabel('Y')
ax2.legend()

plt.tight_layout()
plt.show()
