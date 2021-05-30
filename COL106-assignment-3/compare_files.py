file1 = open("tests/out101.txt", 'r')
file2 = open("tests/hugeout.txt", 'r')
file1 = file1.readlines()
file2 = file2.readlines()
count = 0
for i in range(0, min(len(file1), len(file2))):
	if file1[i] != file2[i]:
		print(i, ": ", file1[i], file2[i])
		count +=1
print(len(file1), len(file2), count)