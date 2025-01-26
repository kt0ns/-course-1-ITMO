number = list(map(int, input().split('=')[1].split('+')))

if len(number) != 1:
    number[len(number) - 1] -= 1
    number[len(number) - 2] += 1
    if number[len(number) - 2] > number[len(number) - 1]:
        number[len(number) - 2] += number[len(number) - 1]
        number = number[:len(number) - 1]
    else:
        while number[len(number) - 2] * 2 <= number[len(number) - 1]:
            number.append(number[len(number) - 1] - number[len(number) - 2])
            number[len(number) - 2] = number[len(number) - 3]

    print(str(sum(number)) + '=' + '+'.join(str(x) for x in number))
else:
    print('No solution')