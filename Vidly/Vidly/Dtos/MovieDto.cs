using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Vidly.Dtos
{
  public class MovieDto
  {
    public int Id { get; set; }

    [Required(ErrorMessage = "Must Enter Movie Name")]
    public string Name { get; set; }

    public GenreDto Genre { get; set; }

    [Display(Name = "Genre")]
    [Required(ErrorMessage = "Must Select Genre")]
    public byte GenreId { get; set; }

    [Display(Name = "Release Date")]
    [Required(ErrorMessage = "Must Enter Release Date")]
    public DateTime ReleaseDate { get; set; }
    public DateTime DateAdded { get; set; }
    public int NumberOfViews { get; set; }
  }
}