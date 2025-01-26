n = int(input())

arr = [x for x in range(1, n+1)]
def podmnojestva(arr, number, s=None):
    resultat = []
    if s == None:
        s = []
    if number == len(arr):
        return [s]

    resultat.extend(podmnojestva(arr, number + 1, s))
    resultat.extend(podmnojestva(arr, number + 1, s + [arr[number]]))
    return resultat

a = sorted(podmnojestva(arr, 0))

for i in a:
    print(*i)
