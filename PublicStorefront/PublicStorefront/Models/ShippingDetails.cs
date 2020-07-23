using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace PublicStorefront.Models
{
  public class ShippingDetails
  {
    [Required(ErrorMessage = "First Name Required")]
    public string FirstName { get; set; }

    [Required(ErrorMessage = "Last Name Required")]
    public string LastName { get; set; }

    [Required(ErrorMessage = "Street Address Required")]
    public string Street { get; set; }

    [Required(ErrorMessage = "City Required")]
    public string City { get; set; }

    [Required(ErrorMessage = "State Required")]
    public string State { get; set; }

    [Required(ErrorMessage = "Zip Code Required")]
    public string Zip { get; set; }
  }
}