using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;

namespace Lab4
{
    class AsyncCallbacks
    {
        public static void run(List<string> hosts)
        {
            for (var i = 0; i < hosts.Count; i++)
            {
                startCallBacks(hosts[i], i);
                Thread.Sleep(2000);
            }
        }

        public static void startCallBacks(string link, int id)
        {
            var linkDns = Dns.GetHostEntry(link.Split('/')[0]);
            var ipAddrs = linkDns.AddressList[0];
            var remoteEndPoint = new IPEndPoint(ipAddrs, Http.PORT);

            var socket = new Socket(ipAddrs.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            //if there is an endpoint we set it
            var endpoint = String.Empty;
            if (link.Contains("/"))
            {
                endpoint = link.Substring(link.IndexOf("/"));
            }
            else
            {
                endpoint = "/";
            }

            //create the socket wrapper since we needed more info than just the socket
            var socketWrap = new SocketWrapper()
            {
                sock = socket,
                hostname = link.Split('/')[0],
                endpoint = endpoint,
                remoteEndPoint = remoteEndPoint,
                id = id
            };

            //we begin the connection
            socket.BeginConnect(remoteEndPoint, startConnect, socketWrap);
        }

        /*
         * function called by the begin connect async callback
         
        */
        public static void startConnect(IAsyncResult socketWrapper)
        {
            //we deserialize the wrapper and get the info
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;
            var hostname = socketWrap.hostname;

            socket.EndConnect(socketWrapper);

            System.Console.WriteLine("Socket {0} connected to {1} (ip:{2})", id, hostname, socket.RemoteEndPoint);

            //get the request that we want to make as a string and
            //convert it to bytes so we can send it with BeginSend

            var requestToBytes = Encoding.ASCII.GetBytes(Http.getRequest(hostname, socketWrap.endpoint));

            //start sending the request as bytes
            socket.BeginSend(requestToBytes, 0, requestToBytes.Length, 0, startSend, socketWrap);
        }

        /*
         * function called by the begin send async callback
         * ar is the socket wrapper
        */
        public static void startSend(IAsyncResult socketWrapper)
        {
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;

            //we get the respone
            var responseInBytes = socket.EndSend(socketWrapper);
            Console.WriteLine("{0} sent {1} bytes to server.", id, responseInBytes);

            //and start receiving
            socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);
        }

        /*
         * function called by the begin receive async callback
         * ar is the socket wrapper
        */
        public static void startReceive(IAsyncResult socketWrapper)
        {
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;


            //Console.WriteLine("yea");

            try
            {
                var requestFromStart = socket.EndReceive(socketWrapper);

                //we unwrap the respons from the bytes recevied and append them to the content container
                socketWrap.responseContent.Append(Encoding.ASCII.GetString(socketWrap.buffer, 0, requestFromStart));
                //Console.WriteLine(Encoding.ASCII.GetString(socketWrap.buffer, 0, requestFromStart));

                //we try to get the respone header
                if (!Http.gotResponseHeader(socketWrap.responseContent.ToString()))
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);
                    //Console.WriteLine("yea");
                }


                //Console.WriteLine("yea");
                var responseBody = Http.getResponseBody(socketWrap.responseContent.ToString());
                var contentLenght = Http.getContentLen(socketWrap.responseContent.ToString());


                //also we keep receiving if we didn't finish reading
                if (responseBody.Length < contentLenght)
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, startReceive, socketWrap);

                }
                //we print everything since there is nothing more to receive
                else
                {

                    System.Console.WriteLine("length is " + contentLenght);

                    socket.Close();
                }
            }
            catch (Exception e)
            {
                System.Console.WriteLine(e.ToString());
            }
        }
    }
}
