using PublicStorefront;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PublicStorefront.Models
{
    public class ItemListViewModel
    {
        public List<Item> Items { get; set; }
        public PagingInfo PagingInfo { get; set; }
    }
}