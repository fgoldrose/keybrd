import string

wordfreqs = {}

def find_freqs(file):	
	with open(file, "r") as f:
		words = ''.join([''.join([i if (i in list(string.ascii_lowercase)) else ' ' for i in line.lower()]) for line in f]).split()
		for w in words:
			if w in wordfreqs:
				wordfreqs[w] +=1
			else:
				wordfreqs[w] = 1

def write_freqs(file):
	with open(file, "w") as f:
		for word in wordfreqs:
			f.write(word + " " + str(wordfreqs[word]) + "\n")

find_freqs("browncorpus.txt")
write_freqs("brownfreqs.txt")