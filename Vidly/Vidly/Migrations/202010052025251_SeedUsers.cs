namespace Vidly.Migrations
{
  using System;
  using System.Data.Entity.Migrations;

  public partial class SeedUsers : DbMigration
  {
    public override void Up()
    {
      Sql(@"
          INSERT INTO [dbo].[AspNetUsers] ([Id], [Email], [EmailConfirmed], [PasswordHash], [SecurityStamp], [PhoneNumber], [PhoneNumberConfirmed], [TwoFactorEnabled], [LockoutEndDateUtc], [LockoutEnabled], [AccessFailedCount], [UserName]) VALUES (N'46e5d050-0202-49c1-897a-aefd5202dafd', N'guest@vidly.com', 0, N'ALxP50BdqnJy0ub6oXsdUSyqt7ZKIw7ceKh4e0yyugyXUufDFPY4G2jtO85im6KQ5Q==', N'a5b1b3d0-e6c7-47d2-84dc-7bda5c450ee6', NULL, 0, 0, NULL, 1, 0, N'guest@vidly.com')
          INSERT INTO [dbo].[AspNetUsers] ([Id], [Email], [EmailConfirmed], [PasswordHash], [SecurityStamp], [PhoneNumber], [PhoneNumberConfirmed], [TwoFactorEnabled], [LockoutEndDateUtc], [LockoutEnabled], [AccessFailedCount], [UserName]) VALUES (N'faf71fad-c0af-43db-abba-97c787a01afc', N'admin@vidly.com', 0, N'AM0Ri8NOihQapoO4hcFgw4YqrDFYEyO+XGMt6pTg298wrv8MPFFJ4T5BKi6W+eTzlA==', N'f8ce4fce-8852-4d2e-a704-4b724a308cb2', NULL, 0, 0, NULL, 1, 0, N'admin@vidly.com')
          INSERT INTO [dbo].[AspNetRoles] ([Id], [Name]) VALUES (N'3fb73ff0-f440-433a-ad07-130cf73c2c6e', N'CanManageMovies')
          INSERT INTO [dbo].[AspNetUserRoles] ([UserId], [RoleId]) VALUES (N'faf71fad-c0af-43db-abba-97c787a01afc', N'3fb73ff0-f440-433a-ad07-130cf73c2c6e')

      ");
    }

    public override void Down()
    {
    }
  }
}
