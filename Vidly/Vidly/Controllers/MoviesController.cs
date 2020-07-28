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
    private AppDbContext _context;

    public MoviesController() 
    {
      _context = new AppDbContext();
    }

    protected override void Dispose(bool disposing)
    {
      _context.Dispose();
    }

    public ActionResult Index()
    {
      var movies = _context.Movies.Include("Genre").ToList();

      return View(movies);
    }

    public ActionResult Details(int id) 
    {
      var current = _context.Movies.Include("Genre").SingleOrDefault(x => x.Id == id);

      return View(current);
    }

    public ActionResult Random()
    {
      var movie = new Movie { Name = "Sunshine", Id = 1 };
      var customers = new List<Customer> {
        new Customer {Id = 1, Name = "Trevor"},
        new Customer { Id = 2, Name = "Sandy"},
        new Customer { Id = 3, Name = "Dane"},
        new Customer { Id = 4, Name = "Pam"}
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