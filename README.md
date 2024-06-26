﻿# JavaUDPClientServer
## Project Description
This repository contains Java programs implementing a UDP client and server. The programs use DatagramSocket and DatagramPacket to send and receive data packets over UDP. The client and server can handle various file types, including text and binary files, with certain size limitations.

## Files
- `udpclient.java`: Java program implementing the UDP client.
- `udpserver.java`: Java program implementing the UDP server.
- `A.txt`, `B.txt`, `C.txt`, `A_161101071.txt`, `B_161101071.txt`: Sample input files for testing.
- `bil452rapor.pdf`: Report describing the implementation and testing of the UDP client and server.

## Java Program Descriptions

### `udpclient.java`
This Java program implements a UDP client that sends data packets to a server. The client uses `DatagramSocket` and `DatagramPacket` to send and receive packets. The client reads data from an input file, converts it to bytes, and sends it to the server.

#### Key Features:
- Reads data from an input file.
- Converts data to bytes and sends it using UDP.
- Receives and processes response packets from the server.

#### Example Usage

DatagramSocket dsoc = new DatagramSocket();
byte[] data = "Sample data".getBytes();
DatagramPacket dp = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 2000);
dsoc.send(dp);

# udpserver.java
This Java program implements a UDP server that receives data packets from clients. The server uses DatagramSocket and DatagramPacket to receive packets and send responses. The server processes incoming data and sends a confirmation response to the client.

# Key Features:
Listens for incoming packets on a specified port.
Processes received data and sends responses.
Handles different file types and sizes, with certain limitations.
# Example Usage

DatagramSocket dsoc = new DatagramSocket(2000);
byte[] buffer = new byte[1024];
DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
dsoc.receive(dp);
String receivedData = new String(dp.getData(), 0, dp.getLength());
Report (bil452rapor.pdf)
The report describes the implementation and testing of the UDP client and server. It includes details on:

The use of DatagramSocket and DatagramPacket for sending and receiving packets.
The port numbers used for communication.
The types of files tested and their size limitations.
The process of converting data to bytes and handling packets.
The importance of password protection for secure communication.
