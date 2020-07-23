using PublicStorefront.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace PublicStorefront.ViewModels
{
  public class CartIndexViewModel
  {
    public Cart Cart { get; set; }
    public string ReturnUrl { get; set; }
  }
}