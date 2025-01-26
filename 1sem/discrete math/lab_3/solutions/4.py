n = int(input())

vect = '0' * n
was = {vect}
for _ in range(2 ** n):
    print(vect)
    vect = vect[1:]
    if vect + '1' not in was:
        vect += '1'
    elif vect + '0' not in was:
        vect += '0'
    else:
        break
    was.add(vect)
