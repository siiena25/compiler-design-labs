# shellcheck disable=SC2044
for filename in $(find test/*.xml)
do
	./xml_parser $filename
done
