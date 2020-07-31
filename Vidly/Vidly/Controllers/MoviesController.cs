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
    private AppDbContext _db;

    public MoviesController() 
    {
      _db = new AppDbContext();
    }

    protected override void Dispose(bool disposing)
    {
      _db.Dispose();
    }

    public ActionResult Index()
    {
      var movies = _db.Movies.Include("Genre").ToList();

      return View(movies);
    }

    public ActionResult Details(int id) 
    {
      var current = _db.Movies.Include("Genre").SingleOrDefault(x => x.Id == id);

      return View(current);
    }

    public ActionResult AddMovie()
    {
      var genres = _db.Genres.ToList();

      var viewModel = new MovieFormViewModel
      {
        Genres = genres
      };

      return View(viewModel);
    }

    public ActionResult EditMovie(int id) 
    {
      var currentMovie = _db.Movies.SingleOrDefault(x => x.Id == id);

      if (currentMovie == null)
      {
        return HttpNotFound();
      }

      var viewModel = new MovieFormViewModel(currentMovie)
      {
        Genres = _db.Genres.ToList(),
      };

      return View("AddMovie", viewModel);
    }

    [HttpPost]
    [ValidateAntiForgeryToken]
    public ActionResult SaveMovie(Movie movie)
    {
      if (!ModelState.IsValid)
      {
        var viewModel = new MovieFormViewModel(movie) {
          Genres = _db.Genres.ToList()
        };

        return View("AddMovie", viewModel);
      }

      if (movie.Id == 0)
      {
        movie.DateAdded = DateTime.Now;
        _db.Movies.Add(movie);
      }
      else 
      {
        var currentMovie = _db.Movies.Single(x => x.Id == movie.Id);

        currentMovie.Name = movie.Name;
        currentMovie.ReleaseDate = movie.ReleaseDate;
        currentMovie.GenreId = movie.GenreId;
      }
      _db.SaveChanges();

      return RedirectToAction("Index", "Movies");
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