n = int(input())
maiki = list(map(int, input().split()))
k = int(input())
pants = list(map(int, input().split()))

l1, l2 = 0, 0
answ = [10 ** 10, 0, 0]

while l1 < n and l2 < k:
    tshirt_color = maiki[l1]
    pants_color = pants[l2]

    current_diff = abs(maiki[l1] - pants[l2])

    if current_diff < answ[0]:
        answ[0] = current_diff
        answ[1] = maiki[l1]
        answ[2] = pants[l2]

    if maiki[l1] < pants[l2]:
        l1 += 1
    else:
        l2 += 1

print(*answ[1:])
