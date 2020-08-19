using AutoMapper;
using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Net;
using System.Web.Http;
using Vidly.Dtos;
using Vidly.Models;

namespace Vidly.Controllers.Api
{
  public class MoviesController : ApiController
  {
    private AppDbContext _db;

    public MoviesController()
    {
      _db = new AppDbContext();
    }

    //GET /api/movies
    public IEnumerable<MovieDto> GetMovies()
    {
      return _db.Movies.Include(x => x.Genre)
                       .ToList()
                       .Select(Mapper.Map<Movie, MovieDto>);
    }

    //GET /api/movies/1
    public IHttpActionResult GetMovie(int id)
    {
      var movie = _db.Movies.SingleOrDefault(x => x.Id == id);

      if (movie == null)
      {
        return NotFound();
      }

      return Ok(Mapper.Map<Movie, MovieDto>(movie));
    }

    //POST /api/movies
    [HttpPost]
    public IHttpActionResult CreateMovie(MovieDto movieDto)
    {
      if (!ModelState.IsValid)
      {
        return BadRequest();
      }

      var movie = Mapper.Map<MovieDto, Movie>(movieDto);

      _db.Movies.Add(movie);
      _db.SaveChanges();

      movieDto.Id = movie.Id;

      return Created(new Uri(Request.RequestUri + "/" + movie.Id), movieDto);
    }

    //PUT api/movie/1
    [HttpPut]
    public void UpdateMovie(int id, MovieDto movieDto)
    {
      if (!ModelState.IsValid)
      {
        throw new HttpResponseException(HttpStatusCode.BadRequest);
      }

      var currentMovie = _db.Movies.SingleOrDefault(x => x.Id == id);

      if (currentMovie == null)
      {
        throw new HttpResponseException(HttpStatusCode.NotFound);
      }

      Mapper.Map(movieDto, currentMovie);

      _db.SaveChanges();
    }

    //DELETE api/movies/1
    [HttpDelete]
    public IHttpActionResult DeleteCustomer(int id)
    {
      var currentMovie = _db.Movies.SingleOrDefault(x => x.Id == id);

      if (currentMovie == null)
      {
        return NotFound();
      }

      _db.Movies.Remove(currentMovie);
      _db.SaveChanges();

      return Ok();
    }
  }
}
