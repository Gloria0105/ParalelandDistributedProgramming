using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Lab4
{
    class AsyncTasks
    {
        private static List<string> HOSTS;

        public static void run(List<string> hostnames)
        {
            HOSTS = hostnames;

            var tasks = new List<Task>();

            for (var i = 0; i < HOSTS.Count; i++)
            {
                tasks.Add(Task.Factory.StartNew(start, i));
            }

            Task.WaitAll(tasks.ToArray());
        }

        private static void start(object idFromStart)
        {
            var id = (int)idFromStart;
            startTask(HOSTS[id], id);
        }

        private static async void startTask(string host, int id)
        {
            var linkDns = Dns.GetHostEntry(host.Split('/')[0]);
            var ipAddress = linkDns.AddressList[0];
            var remoteEndPoint = new IPEndPoint(ipAddress, Http.PORT);
            var socket = new Socket(ipAddress.AddressFamily, SocketType.Stream, ProtocolType.Tcp);

            //if there is an endpoint we set it
            var endpoint = String.Empty;
            if (host.Contains("/"))
            {
                endpoint = host.Substring(host.IndexOf("/"));
            }
            else
            {
                endpoint = "/";
            }

            //create the socket wrapper since we needed more info than just the socket
            var socketWrap = new SocketWrapper()
            {
                sock = socket,
                hostname = host.Split('/')[0],
                endpoint = endpoint,
                remoteEndPoint = remoteEndPoint,
                id = id
            };

            //we start each task and we make them wait until they are signaled
            await connect(socketWrap);

            await send(socketWrap, Http.getRequest(socketWrap.hostname, socketWrap.endpoint));

            await receive(socketWrap);

            //after response from the server is received we print it
            Console.WriteLine(
                "response {0} got : contentLenght = {1}",
                id, Http.getContentLen(socketWrap.responseContent.ToString()));

            //close the socket
            socket.Shutdown(SocketShutdown.Both);
            socket.Close();

        }

        private static async Task connect(SocketWrapper socketWrap)
        {
            //start the connect callback and stop all other threads until it is finished
            socketWrap.sock.BeginConnect(socketWrap.remoteEndPoint, connectCallback, socketWrap);
            await Task.FromResult(socketWrap.connectFinished.WaitOne());
        }

        private static void connectCallback(IAsyncResult socketWrapper)
        {
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;
            var hostname = socketWrap.hostname;

            //end the connection
            socket.EndConnect(socketWrapper);

            System.Console.WriteLine("socket {0} connected to {1} (ip:{2})", id, hostname, socket.RemoteEndPoint);

            //notify the connection is done
            socketWrap.connectFinished.Set();
        }
        private static async Task send(SocketWrapper socketWrap, string request)
        {
            //need the request in bytes so we can send it with begind send
            var requestInBytes = Encoding.ASCII.GetBytes(request);

            //send data to the server
            socketWrap.sock.BeginSend(requestInBytes, 0, requestInBytes.Length, 0, sendCallback, socketWrap);

            //stop all the threads until send is finished
            await Task.FromResult(socketWrap.sendFinished.WaitOne());
        }
        private static void sendCallback(IAsyncResult socketWrapper)
        {
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;
            var id = socketWrap.id;

            //we get the response
            var responseInBytes = socket.EndSend(socketWrapper);
            Console.WriteLine("{0} sent {1} bytes to server.", id, responseInBytes);

            //notify the send finished
            socketWrap.sendFinished.Set();
        }

        private static async Task receive(SocketWrapper socketWrap)
        {
            socketWrap.sock.BeginReceive(socketWrap.buffer, 0, 512, 0, receiveCallback, socketWrap);

            //we block all other threads until the result is finished
            await Task.FromResult(socketWrap.receiveFinished.WaitOne());
        }

        private static void receiveCallback(IAsyncResult socketWrapper)
        {
            var socketWrap = (SocketWrapper)socketWrapper.AsyncState;
            var socket = socketWrap.sock;

            try
            {
                var requestFromStart = socket.EndReceive(socketWrapper);

                //we unwrap the response from the bytes received and append them to the content container
                socketWrap.responseContent.Append(Encoding.ASCII.GetString(socketWrap.buffer, 0, requestFromStart));

                //we re-try if we didn't get a response
                if (!Http.gotResponseHeader(socketWrap.responseContent.ToString()))
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, receiveCallback, socketWrap);
                }


                var responseBody = Http.getResponseBody(socketWrap.responseContent.ToString());
                var contentLenght = Http.getContentLen(socketWrap.responseContent.ToString());


                //also we keep receiving if we didn't finish reading
                if (responseBody.Length < contentLenght)
                {
                    socket.BeginReceive(socketWrap.buffer, 0, 512, 0, receiveCallback, socketWrap);
                }
                //we print everything since there is nothing more to receive
                else
                {
                    socketWrap.receiveFinished.Set();
                }
            }
            catch (Exception e)
            {
                System.Console.WriteLine(e.ToString());
            }
        }
    }
}
