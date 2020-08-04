using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;
using Vidly.Models;

namespace Vidly.ViewModels
{
  public class MovieFormViewModel
  {
    public IEnumerable<Genre> Genres { get; set; }
    public int? Id { get; set; }

    [Required(ErrorMessage = "Must Enter Movie Name")]
    public string Name { get; set; }

    [Display(Name = "Genre")]
    [Required(ErrorMessage = "Must Select Genre")]
    public byte? GenreId { get; set; }

    [Display(Name = "Release Date")]
    [Required(ErrorMessage = "Must Enter Release Date")]
    public DateTime? ReleaseDate { get; set; }
    public int? NumberOfViews { get; set; }

    public MovieFormViewModel() 
    {
      Id = 0;
    }

    public MovieFormViewModel(Movie movie)
    {
      Id = movie.Id;
      Name = movie.Name;
      ReleaseDate = movie.ReleaseDate;
      GenreId = movie.GenreId;
    }
  }
}