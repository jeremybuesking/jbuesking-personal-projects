using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace Vidly.Models
{
  public class Customer
  {
    public int Id { get; set; }

    [Required(ErrorMessage = "Name is required")]
    [StringLength(255, ErrorMessage = "Name must be under 255 characters")]
    public string Name { get; set; }

    [Display(Name = "Date of Birth")]
    [Min18YearsIfAMember]
    public DateTime? Birthdate { get; set; }
    public bool IsSubscribed { get; set; }

    [Display(Name = "Membership Type")]
    public MembershipType MembershipType { get; set; }

    [Required(ErrorMessage = "Must select a membership type")]
    public byte MembershipTypeId { get; set; }
  }
}