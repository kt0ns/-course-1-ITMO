import random


def quickSort_1(array):
    if len(array) <= 1:
        return array

    pivot = array[0]
    left = [el for el in array if el < pivot]
    center = [el for el in array if el == pivot]
    right = [el for el in array if el > pivot]

    return quickSort_1(left) + center + quickSort_1(right)


def quickSort_2(array):
    if len(array) <= 1:
        return array

    pivot_index = random.randrange(len(array))
    pivot = array[pivot_index]

    left = [el for el in array if el < pivot]
    center = [el for el in array if el == pivot]
    right = [el for el in array if el > pivot]

    return quickSort_2(left) + center + quickSort_2(right)


size = input()
data = list(map(int, input().split()))

print(*quickSort_1(data))
print(*quickSort_2(data))
