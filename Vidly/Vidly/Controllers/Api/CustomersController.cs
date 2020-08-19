using AutoMapper;
using System;
using System.Data.Entity;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using Vidly.Dtos;
using Vidly.Models;

namespace Vidly.Controllers.Api
{
  public class CustomersController : ApiController
  {
    private AppDbContext _db;

    public CustomersController()
    {
      _db = new AppDbContext();
    }

    //GET /api/customers
    public IHttpActionResult GetCustomers()
    {
      var customerDtos = _db.Customers
        .Include(x => x.MembershipType)
        .ToList()
        .Select(Mapper.Map<Customer, CustomerDto>);

      return Ok(customerDtos);
    }

    //GET /api/customers/1
    public IHttpActionResult GetCustomer(int id)
    {
      var customer = _db.Customers.SingleOrDefault(x => x.Id == id);

      if (customer == null)
      {
        return NotFound();
      }

      return Ok(Mapper.Map<Customer, CustomerDto>(customer));
    }

    //POST /api/customers
    [HttpPost]
    public IHttpActionResult CreateCustomer(CustomerDto customerDto)
    {
      if (!ModelState.IsValid)
      {
        return BadRequest();
      }

      var customer = Mapper.Map<CustomerDto, Customer>(customerDto);

      _db.Customers.Add(customer);
      _db.SaveChanges();

      customerDto.Id = customer.Id;

      return Created(new Uri(Request.RequestUri + "/" + customer.Id), customerDto);
    }

    //PUT api/customer/1
    [HttpPut]
    public void UpdateCustomer(int id, CustomerDto customerDto)
    {
      if (!ModelState.IsValid)
      {
        throw new HttpResponseException(HttpStatusCode.BadRequest);
      }

      var currentCustomer = _db.Customers.SingleOrDefault(x => x.Id == id);

      if (currentCustomer == null)
      {
        throw new HttpResponseException(HttpStatusCode.NotFound);
      }

      Mapper.Map(customerDto, currentCustomer);

      _db.SaveChanges();
    }

    //DELETE api/customers/1
    [HttpDelete]
    public IHttpActionResult DeleteCustomer(int id)
    {
      var currentCustomer = _db.Customers.SingleOrDefault(x => x.Id == id);

      if (currentCustomer == null)
      {
        return NotFound();
      }

      _db.Customers.Remove(currentCustomer);
      _db.SaveChanges();

      return Ok();
    }
  }
}
