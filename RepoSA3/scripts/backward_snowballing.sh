#! /bin/sh

REPO=$1
OUTPUT_FILE=$2

for file in `find $REPO -name "*.pdf"`;
do
	echo $file
	
	./extract_references.sh $file >> $OUTPUT_FILE
done
