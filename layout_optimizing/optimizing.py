import string
import random

positions = [['' for _ in range(8)] for _ in range(8)]
letters = {}

wordfreqs = {}

# loads from file with format "word frequency" on each line
def load_freqs(file):
	with open(file, "r") as f:
		for line in f:
			x = line.split()
			wordfreqs[x[0]] = int(x[1])

def putl(c, i, j):
	positions[i][j] = c
	positions[j][i] = c
	if c in list(string.ascii_lowercase):
		letters[c] = [i, j]

def switchpos(i1, j1, i2, j2):
	c1 = positions[i1][j1]
	c2 = positions[i2][j2]
	putl(c1, i2, j2)
	putl(c2, i1, j1)

def initrand():
	alphabet = list(string.ascii_lowercase)
	for i in range(7):
		for j in range(i+1, 8):
			alphlen = len(alphabet)
			if alphlen > 0:
				rand = random.randrange(alphlen)
				putl(alphabet[rand], i, j)
				del alphabet[rand]

def clearall():
	for i in range(7):
		for j in range(i+1, 8):
			positions[i][j] = ''
			positions[j][i] = ''
	for l in letters:
		del letters[l]

#saves the current positions to be reloaded later
def savepos(file):
	with open(file, "w") as f:
		for letter in letters:
			f.write(letter + " " + str(letters[letter][0]) + " " + str(letters[letter][1]) + "\n")

def loadpos(file):
	clearall()
	with open(file, "r") as f:
		for line in f:
			x = line.split()
			if len(x) == 3:
				putl(x[0], int(x[1]), int(x[2]))

# the minimum number of times you have to lift finger when writing word
def minlifts(word):
	lifts = 0
	pivot = -1
	for i in range(len(word)-1):
		cur = word[i]
		nxt = word[i+1]
		if pivot == -1:
			if letters[nxt][0] in letters[cur]:
				pivot = letters[nxt][1]
			elif letters[nxt][1] in letters[cur]:
				pivot = letters[nxt][0]
			else:
				lifts +=1
		else:
			if letters[nxt][0] == pivot:
				pivot = letters[nxt][1]
			elif letters[nxt][1] == pivot:
				pivot = letters[nxt][0]
			else:
				pivot = -1
				lifts +=1
	return lifts

# total number of finger lifts when writing the corpus (thing to be minimized)
def totlifts():
	lifts = 0
	for word in wordfreqs:
		lifts += minlifts(word) * wordfreqs[word]
	return lifts

# number of lifts if lifting between every letter
def maxlifts():
	lifts = 0
	for word in wordfreqs:
		lifts += (len(word)-1) * wordfreqs[word]
	return lifts

# keep switching letters when the switch decreases the totlifts
# dumb because it might reach a local minima
def dumb_opt(threshold):
	failed = 0
	curlifts = totlifts()
	while failed < threshold:
		randi1 = random.randrange(7)
		randj1 = random.randrange(randi1+1, 8)
		randi2 = random.randrange(7)
		randj2 = random.randrange(randi2+1, 8)
		switchpos(randi1, randj1, randi2, randj2)
		newlifts = totlifts()
		if newlifts < curlifts:
			curlifts = newlifts
			print(curlifts)
			failed = 0
		else:
			switchpos(randi1, randj1, randi2, randj2) #switch back to old positions
			failed += 1
	return curlifts

def check_localmin():
	curlifts = totlifts()
	for i1 in range(7):
		for j1 in range(i1+1, 8):
			for i2 in range(i1, 7):
				for j2 in range(i2+1, 8):
					if i2 > i1 or j2 > j1:
						switchpos(i1, j1, i2, j2)
						newlifts = totlifts()
						if newlifts < curlifts:
							return False
						else:
							switchpos(i1, j1, i2, j2)
	return True

def do_opt(filename):
	print("BEFORE OPT:\n")
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))
	print (totlifts())
	print("AFTER OPT:")
	dumb_opt(100)
	savepos(filename)
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))

#keeps going as long as a local min has not been reached
def do_min_opt(filename):
	print("BEFORE OPT:\n")
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))
	print (totlifts())
	print("AFTER OPT:")
	print(dumb_opt(50))
	print("checking")
	while not check_localmin():
		print(dumb_opt(50))
		print("checking")
	print(totlifts())
	savepos(filename)
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))

def no_random_opt(filename):
	print("BEFORE OPT:\n")
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))
	print (totlifts())
	print("AFTER OPT:")
	while not check_localmin():
		print(totlifts())
	savepos(filename)

def test(filename):
	load_freqs("brownfreqs.txt")
	loadpos(filename)
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))
	print(minlifts("the"))
	print(minlifts("and"))
	print(check_localmin())
	print(totlifts())
	for l in sorted(letters.keys()):
		print (l + ": " + str(letters[l]))

load_freqs("brownfreqs.txt")
initrand()
no_random_opt("nr1.txt")


