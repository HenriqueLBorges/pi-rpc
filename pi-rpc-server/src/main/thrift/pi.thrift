namespace java com.pi.thrift
#@namespace scala com.pi.thrift

service PiService {
  i64 getNumber()
  void submitNumber(string result)
  string getPI()
}
