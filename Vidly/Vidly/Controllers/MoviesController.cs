using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Vidly.Models;
using Vidly.ViewModels;

namespace Vidly.Controllers
{
  public class MoviesController : Controller
  {
    // GET: Movies
    public ActionResult Index()
    {
      return View();
    }

    public ActionResult Random()
    {
      var movie = new Movie { Name = "Sunshine", Id = 1 };
      var customers = new List<Customer> {
        new Customer {Id = 1, Name = "Trevor"},
        new Customer { Id = 2, Name = "Sandy"},
        new Customer { Id = 2, Name = "Dane"},
        new Customer { Id = 2, Name = "Pam"}
      };

      var viewModel = new RandomMovieViewModel
      {
        Customers = customers,
        Movie = movie
      };

      return View(viewModel);
    }
  }
}