namespace java com.sohu.suc.thrift.gen

struct Person {
    1: string name
    2: i32 age
}

service PersonService{
    string personToString(1: Person person)
}

