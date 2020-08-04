namespace Vidly.Migrations
{
  using System;
  using System.Data.Entity.Migrations;

  public partial class PopulateMoviesTable : DbMigration
  {
    public override void Up()
    {
      Sql("INSERT INTO Movies (Name, GenreId, ReleaseDate, DateAdded, NumberOfViews, Genre_Id) " +
          "VALUES ('Sunshine', 7, '2007-07-20', '2018-01-01', 120, 7)," +
                 "('Get Out', 2, '2017-02-24', '2018-01-01', 211, 2)," +
                 "('The Lion King', 3, '1994-06-24', '2018-01-01', 191, 3)," +
                 "('The Hangover', 5, '2009-06-02', '2018-01-01', 150, 5)," +
                 "('Venom', 1, '2018-10-05', '2019-01-01', 66, 1)," +
                 "('Rainman', 9, '1988-12-12', '2018-01-01', 41, 9)," +
                 "('Edge Of Tomorrow', 7, '2014-05-28', '2019-01-01', 101, 7)");
    }

    public override void Down()
    {
    }
  }
}
