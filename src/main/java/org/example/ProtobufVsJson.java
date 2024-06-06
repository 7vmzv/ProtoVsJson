package org.example;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufVsJson {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final Person person = createLargePerson();

    private static class Data {
        public String name;
        public int id;
        public String email;
        public String[] phoneNumbers;
        public AddressData[] addresses;

        public Data() {}
        public Data(String name, int id, String email, String[] phoneNumbers, AddressData[] addresses) {
            this.name = name;
            this.id = id;
            this.email = email;
            this.phoneNumbers = phoneNumbers;
            this.addresses = addresses;
        }
    }

    private static class AddressData {
        public String street;
        public String city;
        public String state;
        public String zip;

        public AddressData() {}

        public AddressData(String street, String city, String state, String zip) {
            this.street = street;
            this.city = city;
            this.state = state;
            this.zip = zip;
        }
    }

    private static final Data data = createLargeData();

    public static void main(String[] args) throws Exception {
        benchmarkJson();
        benchmarkProtobuf();
    }

    private static void benchmarkJson() throws Exception {
        // Serialization
        long jsonSerializeStart = System.nanoTime();
        String jsonData = objectMapper.writeValueAsString(data);
        long jsonSerializeEnd = System.nanoTime();

        // Deserialization
        long jsonDeserializeStart = System.nanoTime();
        Data deserializedData = objectMapper.readValue(jsonData, Data.class);
        long jsonDeserializeEnd = System.nanoTime();

        // Size
        int jsonSize = jsonData.getBytes().length;

        System.out.println("JSON:");
        System.out.println("  Serialize time: " + (jsonSerializeEnd - jsonSerializeStart) / 1e6 + " ms");
        System.out.println("  Deserialize time: " + (jsonDeserializeEnd - jsonDeserializeStart) / 1e6 + " ms");
        System.out.println("  Size: " + jsonSize + " bytes");
    }

    private static void benchmarkProtobuf() throws InvalidProtocolBufferException {
        // Serialization
        long protoSerializeStart = System.nanoTime();
        byte[] protoData = person.toByteArray();
        long protoSerializeEnd = System.nanoTime();

        // Deserialization
        long protoDeserializeStart = System.nanoTime();
        Person deserializedPerson = Person.parseFrom(protoData);
        long protoDeserializeEnd = System.nanoTime();

        // Size
        int protoSize = protoData.length;

        System.out.println("Protobuf:");
        System.out.println("  Serialize time: " + (protoSerializeEnd - protoSerializeStart) / 1e6 + " ms");
        System.out.println("  Deserialize time: " + (protoDeserializeEnd - protoDeserializeStart) / 1e6 + " ms");
        System.out.println("  Size: " + protoSize + " bytes");
    }

    private static Person createLargePerson() {
        Person.Builder personBuilder = Person.newBuilder()
                .setName("Hamza RBATI")
                .setId(123)
                .setEmail("7vmzv@example.com");

        for (int i = 0; i < 100; i++) {
            personBuilder.addPhoneNumbers("555-010" + i);
        }

        for (int i = 0; i < 100; i++) {
            personBuilder.addAddresses(Address.newBuilder()
                    .setStreet("24 hay iqor")
                    .setCity("Zaouiat chiekh")
                    .setState("BM")
                    .setZip("12345")
                    .build());
        }

        return personBuilder.build();
    }

    private static Data createLargeData() {
        String[] phoneNumbers = new String[100];
        for (int i = 0; i < phoneNumbers.length; i++) {
            phoneNumbers[i] = "555-010" + i;
        }

        AddressData[] addresses = new AddressData[100];
        for (int i = 0; i < addresses.length; i++) {
            addresses[i] = new AddressData("24 hay iqor", "Zaouiat chiekh", "BM", "12345");
        }

        return new Data("Hamza RBATI", 123, "7vmzv@mail.com", phoneNumbers, addresses);
    }
}
