#! /usr/bin/python

import sys
import re

txtfile = sys.argv[1]

def extract_title(reference):
    
	matchObj = re.match('.*and\s\S\S+\.', reference)
	
	print matchObj
    
	if matchObj:
		print matchObj.group()
    
	return



f = open(txtfile, 'r')

toggle = False

ref = []
for line in f.readlines():
	line = line.strip().rstrip('\n').rstrip('\r')
	if line == '':
		continue
	upper_str = line.upper()
	upper_str = upper_str.replace(' ', '')
	if upper_str.endswith("REFERENCES"):
		toggle = True
		continue

	if upper_str.endswith("APPENDIX") or upper_str.endswith("SKETCHES") or upper_str.endswith("ACKNOWLEDGMENTS"):
		toggle = False
		if ref != []:
			bib = " ".join(ref)
			print(bib)
			extract_title(bib)

	if toggle == True:
		if line.startswith('[') or re.match('[0-9]+\.\s.*',line):
			bib = " ".join(ref)
			if ref != []:
                		print(bib)
				extract_title(bib)
			ref = []
			ref.append(line)
		else:
			ref.append(line)

if toggle == True:
	bib = " ".join(ref)
	print(bib)
	extract_title(bib)
