# Source: https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
mkdir $1
cd $1
openssl genrsa -out $1.pem 2048
openssl pkcs8 -topk8 -inform PEM -outform DER -in $1.pem -out $1.der -nocrypt
openssl rsa -in $1.pem -pubout -outform DER -out $1.public.der
