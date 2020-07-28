using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Vidly.Models;

namespace Vidly.Controllers
{
  public class HomeController : Controller
  {
    //private List<Customer> customers = new List<Customer>();
    private List<Movie> movies = new List<Movie>
    {
      new Movie { Id = 1, Name = "Sunshine"},
      new Movie { Id = 1, Name = "Get Out"},
      new Movie { Id = 1, Name = "The Lion King"},
      new Movie { Id = 1, Name = "The Mummy"},
      new Movie { Id = 1, Name = "Rain Man"},
    };

    public ActionResult Index()
    {
      return View();
    }

    public ActionResult Movies()
    {
      ViewBag.Message = "Your movies page.";

      return View(movies);
    }
  }
}