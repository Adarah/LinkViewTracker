const submitDomain = process.env.NEXT_PUBLIC_SUBMIT_DOMAIN;
module.exports = {
  async redirects() {
    return [
      {
        source: '/:url',
        destination: `${submitDomain}/:url`,
        permanent: true,
      },
    ]
  },
}