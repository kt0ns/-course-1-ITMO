data = []

while True:
    str = input()
    if str[1] == 'u':
        data.append(int(str.split()[1]))
        print('ok')
    elif str[0] == 'p':
        print(data[0])
        del data[0]
    elif str[0] == 'f':
        print(data[0])
    elif str[0] == 's':
        print(len(data))
    elif str[0] == 'c':
        data = []
        print('ok')
    else:
        print('bye')
        break
