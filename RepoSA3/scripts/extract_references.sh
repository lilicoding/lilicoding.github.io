#! /bin/sh

PDF_PATH=$1

# pdfx is avaliable at https://github.com/metachris/pdfx
# pdfx relies on pdfminer2, which is available at https://github.com/metachris/pdfminer

PDF_NAME=`basename $PDF_PATH`
cp $PDF_PATH $PDF_NAME

echo "Start to process $PDF_NAME"

pdfx $PDF_NAME -t -o $PDF_NAME.txt

./to_refs.py $PDF_NAME.txt

rm -rf $PDF_NAME
rm -rf $PDF_NAME.txt