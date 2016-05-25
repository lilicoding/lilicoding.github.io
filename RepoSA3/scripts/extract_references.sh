#! /bin/sh

PDF_PATH=$1

PDF_NAME=`basename $PDF_PATH`
cp $PDF_PATH $PDF_NAME

echo "Start to process $PDF_NAME"

pdfx $PDF_NAME -t -o $PDF_NAME.txt

./to_refs.py $PDF_NAME.txt

rm -rf $PDF_NAME
rm -rf $PDF_NAME.txt