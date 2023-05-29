<template>
  <header>
    <div class="block-left">COURSE PROJ</div>
    <div class="block-right">
      <RouterLink to="/home">Home</RouterLink>
      <RouterLink to="/friends">Friends</RouterLink>
      <RouterLink to="/admin">Admin</RouterLink>
      <RouterLink to="/profile">Profile</RouterLink>
    </div>
  </header>
</template>
<style scoped>
header {
  position: sticky;
  top: 0;
  width: 100%;
  box-sizing: border-box;
  background-color: rgba(218, 218, 218, 0.77);

  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding: 1em;
  margin-bottom: 3em;
}
.block-right {
  display: flex;
  flex-direction: row;
  gap: 1em;
}
.router-link-active {
  text-decoration: underline;
  text-decoration-thickness: 2px;
}
a {
  text-decoration: none;
  color: black;
}
</style>
