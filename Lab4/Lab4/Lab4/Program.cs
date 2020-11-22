using System;
using System.Collections.Generic;

namespace Lab4
{
    class Program
    {
        public static List<String> links = new List<String>();

        public static void Main(string[] args)
        {

            links = new List<String> { "netflix.com", "google.com/travel", "olx.ro" };
            AsyncCallbacks.run(links);
            //TaskMechanism.run(links);
            //AsyncTasks.run(links);
        }
    }
}
