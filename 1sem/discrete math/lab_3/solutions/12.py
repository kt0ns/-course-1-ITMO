def gen_s(n, k):
    if n < k:
        return []
    if k == 1:
        return [[[i for i in range(1, n + 1)]]]

    l = [partition + [[n]] for partition in gen_s(n - 1, k - 1)]
    r = []
    for i in gen_s(n - 1, k):
        for j in range(len(i)):
            new_subset = i[j] + [n]
            r.append(i[:j] + [new_subset] + i[j + 1:])
    return l + r


n, k = map(int, input().split())

answ = gen_s(n, k)

for el in answ:
    for subset in el:
        print(" ".join([str(x) for x in subset]))
    print()
